package com.example.leave_management.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    PENDING,
    APPROVED,
    REJECTED;
    @JsonCreator
    public static Status fromJson(String value){
        return  Status.valueOf(value.toUpperCase());
    }


}
