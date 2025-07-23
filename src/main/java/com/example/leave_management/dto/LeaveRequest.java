package com.example.leave_management.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class LeaveRequest {
    @NotNull(message = "start date is required")
    @FutureOrPresent(message = "start date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "end date is required")
    @FutureOrPresent(message = "end date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    private String reason;


}