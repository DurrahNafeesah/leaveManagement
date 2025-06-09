package com.example.leave_management.dto;

import com.example.leave_management.enums.Status;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LeaveStatusUpdate {
    private Status status;

}
