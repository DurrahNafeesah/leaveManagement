package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.enums.Status;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EmailService emailService;
    @CacheEvict(value = "LEAVE", allEntries = true)
    public void applyLeave(LeaveRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer balance=employee.getLeaveBalance();
        if (balance==null || balance<= 0) {
            throw new RuntimeException("Leave balance exhausted. Cannot apply for leave.");
        }

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

    @Async
    @Transactional
    @Cacheable(value = "LEAVE_REPORT",key="'userReport:'+ #email")
    public CompletableFuture<String> getLeaveReportToUser(String email){
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Leave> leaves=leaveRequestRepository.findByUser(employee);
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Start Date,End Date,Reason,Status\n");
        for (Leave leave : leaves) {
            csvBuilder.append(leave.getStartDate()).append(",")
                    .append(leave.getEndDate()).append(",")
                    .append(leave.getReason()).append(",")
                    .append(leave.getStatus()).append("\n");
        }

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        String fileName = "generatedReport-" + employee.getUserName() + ".csv";
        String filePath = System.getProperty("java.io.tmpdir") + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(csvBuilder.toString());
        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException("Error writing report file", e);
        }

        log.info("Report generated at: " + filePath);
        String downloadUrl = "/api/leave/download/file/" + fileName;
        return CompletableFuture.completedFuture(downloadUrl);
    }

    @Cacheable(value = "LEAVE_DETAILS")
    public List<Leave> getTeamLeaves() {
        System.out.println("Fetching team leaves - not from cache");
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return leaveRequestRepository.findByUserManager(manager);
    }
    @Async
    @Transactional
    @Cacheable(value = "LEAVE_BALANCE",key = "'managerReport:'+ #managerEmail")
    public CompletableFuture<String> getLeaveReportToManager(String managerEmail){
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Leave> leaves=leaveRequestRepository.findByUserManager(manager);
        System.out.println("Manager found: " + manager.getUserName());

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Start Date,End Date,Reason,Status\n");

        for (Leave leave : leaves) {
            csvBuilder.append(leave.getStartDate()).append(",")
                    .append(leave.getEndDate()).append(",")
                    .append(leave.getReason()).append(",")
                    .append(leave.getStatus()).append("\n");
        }

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        String fileName = "generatedReport-" + manager.getUserName() + ".csv";
        String filePath = System.getProperty("java.io.tmpdir") + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(csvBuilder.toString());
        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException("Error writing report file", e);
        }

        log.info("Report generated at: " + filePath);
        String downloadUrl = "/api/leave/download/file/" + fileName;
        return CompletableFuture.completedFuture(downloadUrl);

    }

    @Cacheable(value = "LEAVE_DETAILS", key = "'pendingTeamLeaves:' + #managerEmail ")
    public List<Leave> getPendingTeamLeaves() {
        System.out.println("Fetching pending team leaves - not from cache");
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return leaveRequestRepository.findByUserManagerAndStatus(manager, Status.PENDING);
    }

    @Transactional
    @CachePut(value = "LEAVE", key = "#leaveId")
    public void updateLeaveStatus(Long leaveId, Status status) {
        Leave leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!leave.getUser().getManager().getEmail().equals(managerEmail)) {
            throw new RuntimeException("Not authorized to approve this leave");
        }

        String email = leave.getUser().getEmail();

        if (status == Status.APPROVED && leave.getStatus() != Status.APPROVED) {
            User employee = leave.getUser();
            Integer balance = employee.getLeaveBalance();
            if (balance == null || balance <= 0) {
                throw new RuntimeException("Insufficient leave balance");
            }
            employee.setLeaveBalance(balance - 1);
            userRepository.save(employee);

            cacheManager.getCache("LEAVE_BALANCE").evict(email);
            cacheManager.getCache("LEAVE_DETAILS").clear();
        }

        leave.setStatus(status);
        leave.setUpdatedAt(LocalDateTime.now());
        leaveRequestRepository.save(leave);

        emailService.sendLeaveStatusEmail(email, status.name());
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
    @Cacheable(value="LEAVE_BALANCE",key = " #email")
    public int getLeaveBalanceForCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getLeaveBalance();
    }
}
