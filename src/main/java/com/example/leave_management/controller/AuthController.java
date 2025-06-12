package com.example.leave_management.controller;

import com.example.leave_management.dto.*;
import com.example.leave_management.service.AuthService;
import com.example.leave_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

//    @Autowired
//    private UserService userService;
//    @PutMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
////        userService.changePassword(request);
////        return ResponseEntity.ok("Password changed successfully");
////    }
}
