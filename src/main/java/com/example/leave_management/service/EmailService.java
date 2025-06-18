package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to the Leave Management System");
        message.setText("Your account has been created.\nPassword: " + tempPassword );
        mailSender.send(message);


    }

    public void sendLeaveStatusEmail(String to, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Leave Request Status Update");
        message.setText("Your leave request has been " + status + ".");
        mailSender.send(message);
    }

    public void sendApplyLeaveEmail(String to, LeaveRequest request, String employeeName) {
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
    }
}
