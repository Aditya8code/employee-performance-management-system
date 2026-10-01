package com.eps.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utility class for BCrypt password hashing and verification.
 */
public final class PasswordUtil {

    private static final int COST_FACTOR = 12;

    private PasswordUtil() {
        // Private constructor for utility class
    }

    /**
     * Hashes a plain-text password using BCrypt with cost factor 12.
     *
     * @param plainTextPassword plain-text password
     * @return salted BCrypt hash string
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.withDefaults().hashToString(COST_FACTOR, plainTextPassword.toCharArray());
    }

    /**
     * Verifies a plain-text password against a BCrypt hash.
     *
     * @param plainTextPassword plain-text password
     * @param hashedPassword    stored BCrypt hash
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword.toCharArray());
            return result.verified;
        } catch (Exception e) {
            return false;
        }
    }
}
