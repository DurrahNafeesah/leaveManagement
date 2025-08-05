package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Async
    public void sendWelcomeEmail(String to, String tempPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to the Leave Management System");
            message.setText("Your account has been created.\nPassword: " + tempPassword );
            mailSender.send(message);

            log.info("Email send to:{} by thread{}", to, Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("Error sending email to {} by thread{}", to, Thread.currentThread().getName());
        }

    }

    @Async
    public void sendLeaveStatusEmail(String to, String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Leave Request Status Update");
            message.setText("Your leave request has been " + status + ".");
            mailSender.send(message);

            log.info("Email send to:{} by thread{}", to, Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("Error sending email to {} by thread{}", to, Thread.currentThread().getName());
        }
    }

    @Async
    public void sendApplyLeaveEmail(String to, LeaveRequest request, String employeeName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("A new Leave Request Submitted");
            message.setText(
                    "Employee " + employeeName + " has submitted a leave request.\n" +
                            "Reason: " + request.getReason() + "\n" +
                            "Start Date: " + request.getStartDate() + "\n" +
                            "End Date: " + request.getEndDate() + "\n\n" +
                            "Please review it in the Leave Management System."
            );
            mailSender.send(message);

            log.info("Email send to:{} by thread{}", to, Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("Error sending email to {} by thread{}", to, Thread.currentThread().getName());
        }
    }
}
