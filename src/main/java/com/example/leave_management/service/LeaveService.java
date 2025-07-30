package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.enums.Status;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @CacheEvict(value = "LEAVE", key = "#root.authentication.name")
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

        if (employee.getManager() != null) {
            emailService.sendApplyLeaveEmail(
                    employee.getManager().getEmail(),
                    request,
                    employee.getUserName()
            );
        }
    }

    @Cacheable(value = "LEAVE", key = "#root.authentication.name")
    public List<Leave> getMyLeaveHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return leaveRequestRepository.findByUser(employee);
    }

    @Cacheable(value = "LEAVE_DETAILS")
    public List<Leave> getTeamLeaves() {
        System.out.println("Fetching team leaves - not from cache");
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return leaveRequestRepository.findByUserManager(manager);
    }

    @Cacheable(value = "LEAVE_DETAILS", key = "'pendingTeamLeaves:' + #root.authentication.name")
    public List<Leave> getPendingTeamLeaves() {
        System.out.println("Fetching pending team leaves - not from cache");
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return leaveRequestRepository.findByUserManagerAndStatus(manager, Status.PENDING);
    }

    @Transactional
    @CachePut(value = "LEAVE", key = "#leaveId")
    @CacheEvict(value = "LEAVE_DETAILS", allEntries = true)
    public void updateLeaveStatus(Long leaveId, Status status) {
        Leave leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!leave.getUser().getManager().getEmail().equals(managerEmail)) {
            throw new RuntimeException("Not authorized to approve this leave");
        }

        leave.setStatus(status);
        leave.setUpdatedAt(LocalDateTime.now());
        leaveRequestRepository.save(leave);

        emailService.sendLeaveStatusEmail(leave.getUser().getEmail(), status.name());
    }

    @Cacheable("LEAVE_DETAILS")
    public List<Leave> getAllLeaves() {
        return leaveRequestRepository.findAll();
    }

    public Page<Leave> getAllLeaves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return leaveRequestRepository.findAll(pageable);
    }

    public Page<Leave> getLeavesForManager(int page, int size) {
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return leaveRequestRepository.findByUser_Manager(manager, pageable);
    }
}
