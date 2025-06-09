package com.example.leave_management.service;

import com.example.leave_management.dto.UserRegisterRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.enums.Role;
import com.example.leave_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            if (userRepository.exitByEmail(request.getEmail())) {
                throw new RuntimeException("User already exists");
            }

            String tempPassword = generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(tempPassword);

            User user = new User();
            user.setUserName(request.getUserName());
            user.setEmail(request.getEmail());
            user.setRole(Role.valueOf(request.getRole()));
            user.setPassword(encodedPassword);
            user.setCreatedAt(LocalDateTime.now());

            if (request.getManagerId() != null && request.getRole().equalsIgnoreCase("EMPLOYEE")) {
                User manager = userRepository.findById(request.getManagerId())
                        .orElseThrow(() -> new RuntimeException("Manager not found"));
                user.setManager(manager);
            }

            userRepository.save(user);

            emailService.sendWelcomeEmail(user.getEmail(), tempPassword);
        }

        private String generateRandomPassword() {
            return UUID.randomUUID().toString().substring(0, 8);
        }
    }


