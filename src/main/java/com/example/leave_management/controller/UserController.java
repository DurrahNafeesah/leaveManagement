package com.example.leave_management.controller;

import com.example.leave_management.dto.UserRegisterRequest;
import com.example.leave_management.dto.UserResponseDTO;
import com.example.leave_management.util.SwaggerLogsConstants;
import com.example.leave_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin/users")
@Tag(name="User controller APIs",description="create,get,update,delete user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Register user(admin,employee)by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.USER_CREATE_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRegisterRequest request) {
        userService.createUserByAdmin(request);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping
    @Operation(summary = "View all user by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched all User successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.GET_ALL_USER_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsersDto());
}



    @GetMapping("/{id}")
    @Operation(summary = "view user by passing user ID ,by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched User successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.GET_USER_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Updated successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.USER_UPDATE_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<String> updateUser(@PathVariable Long id,@Valid  @RequestBody UserRegisterRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Deleted successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.USER_DELETED_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("pagination/{page}/{size}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated successfully", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = SwaggerLogsConstants.ALL_USER_PAGINATION_SUCCESS)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<Page<UserResponseDTO>> getUsers(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(userService.getAllUsersDto(page,size));
    }
}
