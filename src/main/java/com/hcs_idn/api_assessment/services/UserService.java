package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.request.UserProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.UserRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.UserResponseDTO;
import com.hcs_idn.api_assessment.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(UUID userId);

    UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO);

    void deleteUser(UUID userId);

    UserResponseDTO getCurrentUserProfile();

    UserResponseDTO updateCurrentUserProfile(UserProfileUpdateDTO profileUpdateDTO);
}
