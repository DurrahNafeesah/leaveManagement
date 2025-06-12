package com.example.leave_management.service;


import com.example.leave_management.dto.UserLoginRequest;
import com.example.leave_management.dto.UserRegisterRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.enums.Role;
import com.example.leave_management.repository.UserRepository;
import com.example.leave_management.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;
    public void registerUser(UserRegisterRequest request) {

        if (!Role.ADMIN.name().equalsIgnoreCase(request.getRole())) {
            throw new RuntimeException("Only ADMIN can be registered via this endpoint");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);


        userRepository.save(user);
    }

    public String login(UserLoginRequest request){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail((request.getEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtUtils.generateToken(user.getEmail());
    }

}
