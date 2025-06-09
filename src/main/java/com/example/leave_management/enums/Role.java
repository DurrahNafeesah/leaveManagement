package com.example.leave_management.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    EMPLOYEE,
    MANAGER,
    ADMIN;

    @JsonCreator
    public static Role fromJson(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
