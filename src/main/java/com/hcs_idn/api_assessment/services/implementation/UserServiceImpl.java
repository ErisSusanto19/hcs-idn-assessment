package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.entities.User;
import com.hcs_idn.api_assessment.repositories.UserRepository;
import com.hcs_idn.api_assessment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUserAsEntity(User user){
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkUserByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkUserByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
