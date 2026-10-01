package com.eps.model;

/**
 * Enumeration representing official performance rating bands and their visual badge representations.
 * Scale:
 *   4.50 - 5.00 : Outstanding
 *   3.50 - 4.49 : Exceeds Expectations
 *   2.50 - 3.49 : Meets Expectations
 *   1.50 - 2.49 : Needs Improvement
 *   Below 1.50  : Unsatisfactory
 */
public enum RatingScale {
    OUTSTANDING("Outstanding", 4.50, 5.00, "bg-success text-white", "#198754"),
    EXCEEDS_EXPECTATIONS("Exceeds Expectations", 3.50, 4.499, "bg-primary text-white", "#0d6efd"),
    MEETS_EXPECTATIONS("Meets Expectations", 2.50, 3.499, "bg-info text-dark", "#0dcaf0"),
    NEEDS_IMPROVEMENT("Needs Improvement", 1.50, 2.499, "bg-warning text-dark", "#ffc107"),
    UNSATISFACTORY("Unsatisfactory", 0.00, 1.499, "bg-danger text-white", "#dc3545");

    private final String label;
    private final double minScore;
    private final double maxScore;
    private final String badgeClass;
    private final String colorHex;

    RatingScale(String label, double minScore, double maxScore, String badgeClass, String colorHex) {
        this.label = label;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.badgeClass = badgeClass;
        this.colorHex = colorHex;
    }

    public String getLabel() {
        return label;
    }

    public double getMinScore() {
        return minScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getColorHex() {
        return colorHex;
    }

    public static RatingScale fromScore(double score) {
        if (score >= 4.50) {
            return OUTSTANDING;
        } else if (score >= 3.50) {
            return EXCEEDS_EXPECTATIONS;
        } else if (score >= 2.50) {
            return MEETS_EXPECTATIONS;
        } else if (score >= 1.50) {
            return NEEDS_IMPROVEMENT;
        } else {
            return UNSATISFACTORY;
        }
    }

    public static String getLabelForScore(Double score) {
        if (score == null) {
            return "Pending";
        }
        return fromScore(score).getLabel();
    }

    public static String getBadgeClassForScore(Double score) {
        if (score == null) {
            return "bg-secondary text-white";
        }
        return fromScore(score).getBadgeClass();
    }

    public static String getBadgeClassForLabel(String label) {
        if (label == null) {
            return "bg-secondary text-white";
        }
        for (RatingScale rs : values()) {
            if (rs.getLabel().equalsIgnoreCase(label.trim())) {
                return rs.getBadgeClass();
            }
        }
        return "bg-secondary text-white";
    }
}
