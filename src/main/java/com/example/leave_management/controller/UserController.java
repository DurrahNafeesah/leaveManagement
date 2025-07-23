package com.example.leave_management.controller;

import com.example.leave_management.dto.UserRegisterRequest;
import com.example.leave_management.dto.UserResponseDTO;
import com.example.leave_management.entity.User;
import com.example.leave_management.repository.UserRepository;
import com.example.leave_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Register user(admin,employee)by admin")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRegisterRequest request) {
        userService.createUserByAdmin(request);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping
    @Operation(
            summary = "View all user by admin")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsersDto());
}



    @GetMapping("/{id}")
    @Operation(
            summary = "view user by passing user ID ,by admin")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update the user ")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@Valid  @RequestBody UserRegisterRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("pagination/{page}/{size}")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(userService.getAllUsersDto(page,size));
    }
}
