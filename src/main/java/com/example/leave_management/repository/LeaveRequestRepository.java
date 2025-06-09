package com.example.leave_management.repository;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.entity.leave_requests;
import com.example.leave_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<leave_requests,Long> {
    Optional<leave_requests> findByUser(User user);
    Optional<leave_requests> findByUserManager(User manager);
//    Optional<leave_requests> findByUserBy(User email);

}
