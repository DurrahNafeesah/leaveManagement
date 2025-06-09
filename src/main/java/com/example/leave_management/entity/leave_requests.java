package com.example.leave_management.entity;

import com.example.leave_management.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  leave_requests{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private  Status status;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private  LocalDate end_date;

    @NotNull(message = "start date is required")
    @FutureOrPresent(message = "start date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private  LocalDate start_date;

    @NotBlank(message = "Password is required")
    private  String reason;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;



}
