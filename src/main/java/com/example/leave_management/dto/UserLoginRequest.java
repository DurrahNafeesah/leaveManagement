package com.example.leave_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class UserLoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;


}
