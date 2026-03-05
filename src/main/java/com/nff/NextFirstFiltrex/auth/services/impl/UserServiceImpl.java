package com.nff.NextFirstFiltrex.auth.services.impl;

import com.nff.NextFirstFiltrex.auth.entities.User;
import com.nff.NextFirstFiltrex.auth.repositories.UserRepository;
import com.nff.NextFirstFiltrex.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
