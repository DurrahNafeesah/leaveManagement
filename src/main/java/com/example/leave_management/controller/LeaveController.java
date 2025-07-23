package com.example.leave_management.controller;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.dto.LeaveStatusUpdate;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name="Leave controller APIs",description="leave controller for employee,manager and admin")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // EMPLOYEE
    @PostMapping("/employee/leave/apply")
    @Operation(
            summary = "Employee can register here")
    public ResponseEntity<String> applyLeave(@RequestBody @Valid LeaveRequest request) {
        leaveService.applyLeave(request);
        return ResponseEntity.ok("Leave applied successfully");
    }

    @GetMapping("/employee/leave")
    @Operation(
            summary = "Employee can fetch their leave")
    public ResponseEntity<List<Leave>> getMyLeaveHistory() {
        return ResponseEntity.ok(leaveService.getMyLeaveHistory());
    }

    // MANAGER
    @GetMapping("/manager/leave/team")
    @Operation(
            summary = "Manager can fetch their team's leave")
    public ResponseEntity<List<Leave>> getTeamLeaves() {
        return ResponseEntity.ok(leaveService.getTeamLeaves());
    }

    @GetMapping("/manager/leave/pending")
    @Operation(
            summary = "Manager to fetch pending leave")
    public ResponseEntity<List<Leave>> getPendingTeamLeaves() {
        return ResponseEntity.ok(leaveService.getPendingTeamLeaves());
    }

    @PutMapping("/manager/leave/update-status/{leaveId}")
    @Operation(
            summary = "Manger can update the leave by passing leave ID ")
    public ResponseEntity<String> updateLeaveStatus(@PathVariable Long leaveId, @RequestBody LeaveStatusUpdate status) {
        leaveService.updateLeaveStatus(leaveId, status.getStatus());
        return ResponseEntity.ok("Leave status updated");
    }

    @GetMapping("/manager/leave/team/pagination/{page}/{size}")
    public ResponseEntity<Page<Leave>> getLeaveForManager(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok( leaveService.getLeavesForManager(page, size));
    }
    // ADMIN
    @GetMapping("/admin/leave/all")
    @Operation(
            summary = "Admin can view all leave")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/admin/leave/all/pagination/{page}/{size}")
    public ResponseEntity<Page<Leave>> getAllLeaves(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(leaveService.getAllLeaves(page, size));
    }
}