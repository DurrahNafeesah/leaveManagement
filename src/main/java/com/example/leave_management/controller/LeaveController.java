package com.example.leave_management.controller;

import com.example.leave_management.dto.LeaveRequest;
import com.example.leave_management.dto.LeaveStatusUpdate;
import com.example.leave_management.entity.Leave;
import com.example.leave_management.entity.User;
import com.example.leave_management.service.LeaveBalanceService;
import com.example.leave_management.service.LeaveService;
import com.example.leave_management.util.SwaggerLogsConstants;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@Tag(name="Leave controller APIs",description="leave controller for employee,manager and admin")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    // EMPLOYEE
    @PostMapping("/employee/leave/apply")
    @Operation(summary = "Employee can apply leave here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee Applied leave successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_SUCCESS_RESPONSE)
            })),
            @ApiResponse(responseCode = "400", description = "Validation failed or bad input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> applyLeave(@RequestBody @Valid LeaveRequest request) {
        leaveService.applyLeave(request);
        return ResponseEntity.ok("Leave applied successfully");
    }

    @GetMapping("/employee/leave")
    @Operation(summary = "Employee can view leave history their leave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "leave history fetched successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_HISTORY_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Leave>> getMyLeaveHistory() {
        return ResponseEntity.ok(leaveService.getMyLeaveHistory());
    }
    @GetMapping("/employee/leave/download")
    @Operation(summary = "Employee can download leave history ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "leave history downloaded successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_HISTORY_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> getLeaveReportToUser() throws ExecutionException, InterruptedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CompletableFuture<String> future = leaveService.getLeaveReportToUser(email);

        String result = future.get();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/employee/leave/balance")
    @Operation(summary = "Employee can view their leave balance ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched leave balance successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_BALANCE_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Integer> getLeaveBalance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(leaveService.getLeaveBalanceForCurrentUser(email));
    }



    // MANAGER
    @GetMapping("/manager/leave/team/download")
    @Operation(summary = "Manager can download their team's leave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "leave history Downloaded successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_HISTORY_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> getLeaveReportToManager() throws ExecutionException, InterruptedException {
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CompletableFuture<String> future = leaveService.getLeaveReportToManager(managerEmail);

        String result = future.get();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/manager/leave/team")
    @Operation(summary = "Manager can fetch their team's leave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "leave history fetched successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_HISTORY_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Leave>> getTeamLeaves() {
        return ResponseEntity.ok(leaveService.getTeamLeaves());
    }


    @GetMapping("/manager/leave/pending")
    @Operation(summary = "Manager to fetch pending leave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched pending leave successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_PENDING_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Leave>> getPendingTeamLeaves() {
        return ResponseEntity.ok(leaveService.getPendingTeamLeaves());
    }

    @PutMapping("/manager/leave/update-status/{leaveId}")
    @Operation(summary = "Manger can update the leave by passing leave ID ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated leave  successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.LEAVE_UPDATE_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> updateLeaveStatus(@PathVariable Long leaveId, @RequestBody LeaveStatusUpdate status) {
        leaveService.updateLeaveStatus(leaveId, status.getStatus());
        return ResponseEntity.ok("Leave status updated");
    }

    @GetMapping("/manager/leave/team/pagination/{page}/{size}")
    @Operation(summary = "manager can view all leave with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated leaves fetched successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.ALL_LEAVES_PAGINATION_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<Leave>> getLeaveForManager(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok( leaveService.getLeavesForManager(page, size));
    }

    // ADMIN
    @GetMapping("/admin/leave/all")
    @Operation(summary = "Admin can view all leave with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated leaves fetched successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success Response", value = SwaggerLogsConstants.ALL_LEAVES_PAGINATION_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/admin/leave/all/pagination/{page}/{size}")
    public ResponseEntity<Page<Leave>> getAllLeaves(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(leaveService.getAllLeaves(page, size));
    }

    @GetMapping("/leave/download/file/{fileName}")
    public ResponseEntity<Resource> downloadReport(@PathVariable String fileName) throws IOException, FileNotFoundException {
        String filePath = System.getProperty("java.io.tmpdir") + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}