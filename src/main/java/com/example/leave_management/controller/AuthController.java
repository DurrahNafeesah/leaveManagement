package com.example.leave_management.controller;

import com.example.leave_management.dto.*;
import com.example.leave_management.service.AuthService;
import com.example.leave_management.util.SwaggerLogsConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name="Auth controller APIs",description="login and register for the admin")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Login with email and password to get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token received", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LOGIN_SUCCESS_RESPONSE)
            })),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    @Operation(summary = "User(admin,manager,employee)can register here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Registered Successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.REGISTER_SUCCESS_RESPONSE)
            })),
            @ApiResponse(responseCode = "400", description = "Validation failed or bad input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

}
