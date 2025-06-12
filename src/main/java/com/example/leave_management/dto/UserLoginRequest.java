package com.example.leave_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class UserLoginRequest {
    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;


}
