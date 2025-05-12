package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.entities.User;

import java.util.UUID;

public interface UserService {
    User addUserAsEntity(User user);
    Boolean checkUserByUsername(String username);
    Boolean checkUserByEmail(String email);
//    User findUserByIdAsEntity(UUID id);
}
