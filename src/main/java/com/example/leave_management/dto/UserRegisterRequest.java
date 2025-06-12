package com.example.leave_management.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class UserRegisterRequest {

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "email is required")
    @Email(message="invalid email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank
    private String role;

    private Long managerId;
    @AssertTrue(message = "ManagerId can only be set when role is EMPLOYEE")
    public boolean isManagerValidForEmployee() {
        if ("EMPLOYEE".equalsIgnoreCase(role)) {
            return managerId != null;
        }
        return true;
    }

}
