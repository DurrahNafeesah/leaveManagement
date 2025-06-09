package com.example.leave_management.dto;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor


public class UserLoginResponse {
    private String username;
    private String role;
    private String token;
}
