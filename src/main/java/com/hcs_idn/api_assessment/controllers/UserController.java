package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.UserProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.UserRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.UserResponseDTO;
import com.hcs_idn.api_assessment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<UserResponseDTO>> getCurrentUserProfile() {
        UserResponseDTO userProfile = userService.getCurrentUserProfile();
        BaseResponse<UserResponseDTO> response = BaseResponse.<UserResponseDTO>builder()
                .message("User profile retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(userProfile)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<UserResponseDTO>> updateCurrentUserProfile(@RequestBody UserProfileUpdateDTO profileUpdateDTO) {
        UserResponseDTO updatedProfile = userService.updateCurrentUserProfile(profileUpdateDTO);
        BaseResponse<UserResponseDTO> response = BaseResponse.<UserResponseDTO>builder()
                .message("User profile updated successfully.")
                .code(HttpStatus.OK.value())
                .data(updatedProfile)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<UserResponseDTO>> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        BaseResponse<UserResponseDTO> response = BaseResponse.<UserResponseDTO>builder()
                .message("User created successfully by admin.")
                .code(HttpStatus.CREATED.value())
                .data(createdUser)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        BaseResponse<List<UserResponseDTO>> response = BaseResponse.<List<UserResponseDTO>>builder()
                .message("All users retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        BaseResponse<UserResponseDTO> response = BaseResponse.<UserResponseDTO>builder()
                .message("User retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<UserResponseDTO>> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        BaseResponse<UserResponseDTO> response = BaseResponse.<UserResponseDTO>builder()
                .message("User updated successfully by admin.")
                .code(HttpStatus.OK.value())
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .message("User deleted successfully.")
                .code(HttpStatus.OK.value())
                .data("User with id " + id + " was deleted.")
                .build();
        return ResponseEntity.ok(response);
    }
}
