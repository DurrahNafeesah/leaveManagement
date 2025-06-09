package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.entity.User;
import com.example.leave_management.enums.Status;
import com.example.leave_management.repository.LeaveRepository;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
import com.example.leave_management.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService<Leave> {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Employee applies for leave
     */
    public void applyLeave(LeaveRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Leave leave = new Leave();
        leave.setUser(employee);
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus(Status.PENDING);
        leave.setAppliedAt(LocalDateTime.now());

        leaveRequestRepository.save(leave);
    }

    /**
     * Employee can view their own leave history
     */
    public List<Leave> getMyLeaveHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return leaveRequestRepository.findByUser(employee);
    }

    /**
     * Manager can view team member leave requests
     */
    public List<Leave> getTeamLeaves() {
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return leaveRepository.findByUserManager(manager);
    }

    /**
     * Manager approves or rejects leave
     */
    @Transactional
    public void updateLeaveStatus(Long leaveId, Status status) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!leave.getUser().getManager().getEmail().equals(managerEmail)) {
            throw new RuntimeException("Not authorized to approve this leave");
        }

        leave.setStatus(status);
        leave.setUpdatedAt(LocalDateTime.now());
        leaveRepository.save(leave);

        // Send email notification
        emailService.sendLeaveStatusEmail(leave.getUser().getEmail(), status.name());
    }

    /**
     * Admin views all leave requests
     */
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }
}
