package com.example.leave_management.util;

public class SwaggerLogsConstants {
    public static final String LOGIN_SUCCESS_RESPONSE = "{ \"token\": \"eyJhbGciOi...\" }";
    public static final String REGISTER_SUCCESS_RESPONSE = "\"User registered successfully\"";
    public static final String ERROR_500 = "{ \"error\": \"Internal Server Error\" }";
    public static final String LEAVE_SUCCESS_RESPONSE="\"Applied leave successfully\"";
    public static final String LEAVE_HISTORY_SUCCESS="[{\"leaveId\":1,\"reason\":\"Sick\",\"StartDate\":\"2025-01-01\",\"EndDate\":\"2025-01-07\"}]}";
    public static final String LEAVE_PENDING_SUCCESS="[ { \"leaveId\": 5, \"employeeName\": \"John Doe\", \"reason\": \"SICK\", \"startDate\": \"2025-07-25\", \"endDate\": \"2025-07-26\", \"status\": \"PENDING\" } ]";
    public static final String LEAVE_UPDATE_SUCCESS="\"Leave Updated successfully\"";
    public static final String ALL_LEAVES_PAGINATION_SUCCESS= "{ \"content\": [ { \"leaveId\": 1, \"reason\": \"SICK\", \"startDate\": \"2025-07-01\", \"endDate\": \"2025-07-03\", \"status\": \"APPROVED\" } ], \"totalPages\": 3, \"totalElements\": 21 }";
    public static final String USER_CREATE_SUCCESS="\"User Created successfully\"";
    public static final String USER_UPDATE_SUCCESS="\"User Updated successfully\"";
    public static final String GET_ALL_USER_SUCCESS="[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}]";
    public static final String GET_USER_SUCCESS="[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}]";
    public static final String USER_DELETED_SUCCESS="\"User Deleted successfully\"";
    public static final String ALL_USER_PAGINATION_SUCCESS="{ \"content\": [{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}], \"totalPages\": 3, \"totalElements\": 21 }";
    public static final String LEAVE_BALANCE_SUCCESS="\"Balance :2\"";
}
