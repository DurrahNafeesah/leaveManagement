package com.example.leave_management.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class LeaveRequest {
    @NotBlank
    private  LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
    @NotBlank
    private  String reason;


//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return end_date;
//    }
//
//    public void getEndDate(LocalDate end_date) {
//        this.end_date = end_date;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
    
}
