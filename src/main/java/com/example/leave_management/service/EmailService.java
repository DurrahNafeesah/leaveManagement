package com.example.leave_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to the Leave Management System");
        message.setText("Your account has been created.\\nTemporary Password: \" + tempPassword + \"\\nPlease change your password after login.");
        mailSender.send(message);


    }
    public void sendLeaveStatusEmail(String to, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Leave Request Status Update");
        message.setText("Your leave request has been " + status + ".");
        mailSender.send(message);
    }
}
