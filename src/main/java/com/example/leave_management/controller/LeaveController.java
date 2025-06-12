package com.example.leave_management.controller;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.dto.LeaveStatusUpdate;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // EMPLOYEE
    @PostMapping("/employee/leave/apply")
    public ResponseEntity<String> applyLeave(@RequestBody LeaveRequest request) {
        leaveService.applyLeave(request);
        return ResponseEntity.ok("Leave applied successfully");
    }

    @GetMapping("/employee/leave")
    public ResponseEntity<List<Leave>> getMyLeaveHistory() {
        return ResponseEntity.ok(leaveService.getMyLeaveHistory());
    }

    // MANAGER
    @GetMapping("/manager/leave/team")
    public ResponseEntity<List<Leave>> getTeamLeaves() {
        return ResponseEntity.ok(leaveService.getTeamLeaves());
    }

    @GetMapping("/manager/leave/pending")
    public ResponseEntity<List<Leave>> getPendingTeamLeaves() {
        return ResponseEntity.ok(leaveService.getPendingTeamLeaves());
    }

    @PutMapping("/manager/leave/update-status/{leaveId}")
    public ResponseEntity<String> updateLeaveStatus(@PathVariable Long leaveId, @RequestBody LeaveStatusUpdate status) {
        leaveService.updateLeaveStatus(leaveId, status.getStatus());
        return ResponseEntity.ok("Leave status updated");
    }

    // ADMIN
    @GetMapping("/admin/leave/all")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }
}