package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.request.UserProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.UserRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.UserResponseDTO;
import com.hcs_idn.api_assessment.entities.Account;
import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.entities.User;
import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.exceptions.customs.BadRequest;
import com.hcs_idn.api_assessment.exceptions.customs.Forbidden;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.AccountRepository;
import com.hcs_idn.api_assessment.repositories.RoleRepository;
import com.hcs_idn.api_assessment.repositories.UserRepository;
import com.hcs_idn.api_assessment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        if (accountRepository.findByUsername(requestDTO.getUsername()).isPresent() ||
                accountRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new BadRequest("Username or Email already exists!");
        }

        Set<Role> roles = new HashSet<>();
        for (UserRole roleName : requestDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }

        Account account = new Account();
        account.setUsername(requestDTO.getUsername());
        account.setEmail(requestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        account.setRoles(roles);

        User user = new User();
        user.setFullName(requestDTO.getFullName());
        user.setAccount(account);

        User savedUser = userRepository.save(user);

        return mapToUserResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFound("User not found with id: " + userId));
        return mapToUserResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID userId, UserRequestDTO requestDTO) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new NotFound("User not found with id: " + userId));

        userToUpdate.setFullName(requestDTO.getFullName());

        Account account = userToUpdate.getAccount();
        account.setUsername(requestDTO.getUsername());
        account.setEmail(requestDTO.getEmail());

        if (StringUtils.hasText(requestDTO.getPassword())) {
            account.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        Set<Role> roles = new HashSet<>();
        for (UserRole roleName : requestDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new NotFound("Role not found: " + roleName));
            roles.add(role);
        }
        account.setRoles(roles);

        User updatedUser = userRepository.save(userToUpdate);
        return mapToUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUserProfile() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Logged in user not found in database."));

        if (account.getUser() == null) {
            throw new Forbidden("This endpoint is for staff users only.");
        }

        return mapToUserResponseDTO(account.getUser());
    }

    @Override
    @Transactional
    public UserResponseDTO updateCurrentUserProfile(UserProfileUpdateDTO profileUpdateDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found in database."));

        if (account.getUser() == null) {
            throw new IllegalStateException("This endpoint is for staff users only.");
        }

        User userToUpdate = account.getUser();
        userToUpdate.setFullName(profileUpdateDTO.getFullName());

        User updatedUser = userRepository.save(userToUpdate);
        return mapToUserResponseDTO(updatedUser);
    }

    // --- Mapper Helper Method ---
    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getAccount() != null) {
            dto.setUsername(user.getAccount().getUsername());
            dto.setEmail(user.getAccount().getEmail());
            Set<UserRole> roleNames = user.getAccount().getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }
        return dto;
    }
}
