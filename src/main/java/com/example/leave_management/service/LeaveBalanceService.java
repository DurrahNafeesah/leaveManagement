package com.example.leave_management.service;

import com.example.leave_management.entity.User;
import com.example.leave_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LeaveBalanceService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(cron ="0 0 0 1 * ?")
    @CacheEvict(value="LEAVE_BALANCE",allEntries = true)
    public void addMonthlyLeaveBalance() {
        try {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                Integer currentBalance = user.getLeaveBalance() != null ? user.getLeaveBalance() : 0;
                user.setLeaveBalance(currentBalance + 2);
            }
            userRepository.saveAll(users);
        log.info("Leave credited successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
