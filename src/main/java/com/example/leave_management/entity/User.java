package com.example.leave_management.entity;

import com.example.leave_management.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private  String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private  String email;

    @NotBlank(message = "Password is required")
    private  String password;

    @Enumerated(EnumType.STRING)
    private  Role role;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private User manager;

    @OneToMany(mappedBy = "manager")
    @JsonManagedReference
    private List<User> teamMembers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Leave> leaves;

    @Column(name = "leave_balance")
    private Integer leaveBalance = 0;


}
