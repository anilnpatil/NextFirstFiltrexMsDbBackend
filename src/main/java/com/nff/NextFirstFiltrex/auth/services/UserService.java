package com.nff.NextFirstFiltrex.auth.services;

import com.nff.NextFirstFiltrex.auth.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    boolean deleteUser(Long id);
}
