package com.eps.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Input validation utility class for user, evaluation, and criteria data.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidScore(int score) {
        return score >= 1 && score <= 5;
    }

    public static boolean isValidWeight(BigDecimal weight) {
        return weight != null && weight.compareTo(BigDecimal.ZERO) > 0 && weight.compareTo(BigDecimal.valueOf(100)) <= 0;
    }
}
