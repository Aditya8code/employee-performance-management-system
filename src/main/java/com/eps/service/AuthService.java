package com.eps.service;

import com.eps.model.User;

/**
 * Service interface for authentication and credential security.
 */
public interface AuthService {

    User authenticate(String usernameOrEmail, String plainPassword);

    void changePassword(Integer userId, String oldPassword, String newPassword);
}
