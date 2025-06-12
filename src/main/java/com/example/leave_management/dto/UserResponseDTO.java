package com.example.leave_management.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Long managerId;
    private List<Long> teamMemberIds;
}
