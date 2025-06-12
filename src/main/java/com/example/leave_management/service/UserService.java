package com.example.leave_management.service;

import com.example.leave_management.dto.UserRegisterRequest;
import com.example.leave_management.dto.UserResponseDTO;
import com.example.leave_management.entity.User;
import com.example.leave_management.enums.Role;
import com.example.leave_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private EmailService emailService;

    public void createUserByAdmin(UserRegisterRequest request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Only ADMIN can create users");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        // Validate allowed roles
        Role newUserRole = Role.valueOf(request.getRole().toUpperCase());
        if (newUserRole == Role.ADMIN) {
            throw new RuntimeException("ADMIN creation is not allowed via this endpoint");
        }

        String tempPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User newUser = new User();
        newUser.setUserName(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setRole(newUserRole);
        newUser.setCreatedAt(LocalDateTime.now());

        if (newUserRole == Role.EMPLOYEE) {
            if (request.getManagerId() == null) {
                throw new RuntimeException("EMPLOYEE must have a manager assigned");
            }

            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            if (!manager.getRole().equals(Role.MANAGER)) {
                throw new RuntimeException("Assigned manager must have role MANAGER");
            }

            newUser.setManager(manager);
        }else {
            // For MANAGER or other roles, managerId must NOT be set
            if (request.getManagerId() != null) {
                throw new RuntimeException(newUserRole + " should not have a manager assigned");
            }
            newUser.setManager(null);
        }


        userRepository.save(newUser);
        emailService.sendWelcomeEmail(newUser.getEmail(), tempPassword);
    }

//    public  void changePassword(PasswordChangeRequest request) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
//            throw new RuntimeException("Old password is incorrect");
//        }
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//    }



    public Optional<User> getUserById(Long id) {
            return userRepository.findById(id);

            
    }

    public void updateUser(Long id, UserRegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setRole(Role.fromJson(request.getRole()));

        if (request.getRole().equalsIgnoreCase("EMPLOYEE")) {
            if (request.getManagerId() == null) {
                throw new RuntimeException("EMPLOYEE must have a managerId");
            }

            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            if (!manager.getRole().equals(Role.MANAGER)) {
                throw new RuntimeException("Assigned manager must have role MANAGER");
            }

            user.setManager(manager);

        }else {
            // For MANAGER and ADMIN, no managerId should be set
            if (request.getManagerId() != null) {
                throw new RuntimeException(request.getRole() + " should not have a managerId");
            }
            user.setManager(null);
        }

        userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public List<UserResponseDTO> getAllUsersDto() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).toList();
    }

    private UserResponseDTO convertToDto(User user) {
        List<Long> teamIds = null;

        if (user.getRole() == Role.MANAGER && user.getTeamMembers() != null) {
            teamIds = user.getTeamMembers().stream().map(User::getUserId).toList();
        }

        return new UserResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getManager() != null ? user.getManager().getUserId() : null,
                teamIds
        );
    }


    private String generateRandomPassword() {

            return UUID.randomUUID().toString().substring(0, 8);
    }

}


