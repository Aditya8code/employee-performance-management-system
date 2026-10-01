package com.eps.dao;

import com.eps.model.User;

import java.util.List;

/**
 * Data Access Object for User operations.
 */
public interface UserDAO extends GenericDAO<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findByRole(String role);

    boolean updatePassword(Integer userId, String newPasswordHash);

    boolean updateStatus(Integer userId, String status);
}
