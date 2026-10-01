package com.eps.service.impl;

import com.eps.dao.UserDAO;
import com.eps.dao.impl.UserDAOImpl;
import com.eps.exception.AuthenticationException;
import com.eps.exception.ValidationException;
import com.eps.model.User;
import com.eps.service.AuthService;
import com.eps.util.PasswordUtil;

/**
 * Implementation of AuthService with BCrypt password verification.
 */
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;

    public AuthServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User authenticate(String usernameOrEmail, String plainPassword) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() ||
            plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new AuthenticationException("Username and password are required.");
        }

        String identifier = usernameOrEmail.trim();
        User user = userDAO.findByUsername(identifier);
        if (user == null) {
            user = userDAO.findByEmail(identifier);
        }

        if (user == null) {
            throw new AuthenticationException("Invalid username/email or password.");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive. Please contact your system administrator.");
        }

        boolean matched = PasswordUtil.checkPassword(plainPassword, user.getPasswordHash());
        if (!matched) {
            throw new AuthenticationException("Invalid username/email or password.");
        }

        return user;
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new ValidationException("New password must be at least 6 characters long.");
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            throw new AuthenticationException("User account not found.");
        }

        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Current password does not match.");
        }

        String newHash = PasswordUtil.hashPassword(newPassword);
        userDAO.updatePassword(userId, newHash);
    }
}
