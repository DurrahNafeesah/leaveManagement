package com.example.leave_management.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class LeaveRequest {
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
    @NotBlank
    private String reason;


}