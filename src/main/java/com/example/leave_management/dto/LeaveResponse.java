package com.example.leave_management.dto;

import com.example.leave_management.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class LeaveResponse {
    private  Long id;
    private String employeeName;
    private LocalDate start_date;
    private LocalDate end_date;
    private String reason;
    private Status status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
