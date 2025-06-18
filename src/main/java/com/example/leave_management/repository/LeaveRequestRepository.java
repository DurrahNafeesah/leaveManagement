package com.example.leave_management.repository;

import com.example.leave_management.entity.Leave;
import com.example.leave_management.entity.User;
import com.example.leave_management.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<Leave,Long> {
    List<Leave> findByUser(User user);
    List<Leave> findByUserManager(User manager);

    List<Leave> findByUserManagerAndStatus(User manager, Status status);
//    Optional<leave_requests> findByUserBy(User email);

}
