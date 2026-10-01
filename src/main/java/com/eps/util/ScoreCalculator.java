package com.eps.util;

import com.eps.model.EvaluationScore;
import com.eps.model.RatingScale;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Business utility for scoring calculations and rating category mappings.
 * Rating bands:
 *   4.50 - 5.00 : Outstanding
 *   3.50 - 4.49 : Exceeds Expectations
 *   2.50 - 3.49 : Meets Expectations
 *   1.50 - 2.49 : Needs Improvement
 *   Below 1.50  : Unsatisfactory
 */
public final class ScoreCalculator {

    private ScoreCalculator() {
    }

    /**
     * Calculates the weighted average score from a list of criterion scores.
     * Formula: Total Score = (Sum(score_i * weight_i)) / Sum(weight_i)
     *
     * @param scores List of EvaluationScore items
     * @return BigDecimal rounded to 2 decimal places, or null if scores empty
     */
    public static BigDecimal calculateWeightedScore(List<EvaluationScore> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }

        BigDecimal totalWeighted = BigDecimal.ZERO;
        BigDecimal totalWeights = BigDecimal.ZERO;

        for (EvaluationScore es : scores) {
            if (es.getCriterionWeight() != null && es.getCriterionWeight().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal weight = es.getCriterionWeight();
                totalWeights = totalWeights.add(weight);
                totalWeighted = totalWeighted.add(BigDecimal.valueOf(es.getScore()).multiply(weight));
            }
        }

        if (totalWeights.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return totalWeighted.divide(totalWeights, 2, RoundingMode.HALF_UP);
    }

    /**
     * Determines rating category label based on total score.
     */
    public static String getRatingLabel(BigDecimal score) {
        if (score == null) {
            return "Pending";
        }
        return RatingScale.getLabelForScore(score.doubleValue());
    }

    /**
     * Determines rating badge CSS class based on total score.
     */
    public static String getRatingBadgeClass(BigDecimal score) {
        if (score == null) {
            return "bg-secondary text-white";
        }
        return RatingScale.getBadgeClassForScore(score.doubleValue());
    }
}
