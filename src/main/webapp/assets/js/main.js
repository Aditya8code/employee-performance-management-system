/**
 * Employee Performance Management System - Main JavaScript
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Auto-dismiss flash alerts after 5 seconds
    const alerts = document.querySelectorAll(".alert-dismissible");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });

    // 2. Real-time Live Weighted Score Calculator for Evaluation Form
    initLiveScoreCalculator();
});

function initLiveScoreCalculator() {
    const scoreInputs = document.querySelectorAll(".criterion-score-input");
    if (!scoreInputs || scoreInputs.length === 0) return;

    function recalculate() {
        let totalWeighted = 0;
        let totalWeights = 0;
        let answeredCount = 0;

        const criteriaWeights = {};
        const criteriaScores = {};

        // Find all radio inputs checked
        document.querySelectorAll(".criterion-score-input:checked").forEach(function (radio) {
            const criterionId = radio.getAttribute("data-criterion-id");
            const weight = parseFloat(radio.getAttribute("data-weight")) || 0;
            const score = parseInt(radio.value, 10);

            criteriaWeights[criterionId] = weight;
            criteriaScores[criterionId] = score;
            answeredCount++;
        });

        for (const cid in criteriaScores) {
            const w = criteriaWeights[cid];
            const s = criteriaScores[cid];
            totalWeights += w;
            totalWeighted += (s * w);
        }

        const scorePreviewEl = document.getElementById("liveTotalScore");
        const ratingLabelEl = document.getElementById("liveRatingLabel");
        const ratingBadgeEl = document.getElementById("liveRatingBadge");

        if (totalWeights > 0 && answeredCount > 0) {
            const finalScore = (totalWeighted / totalWeights).toFixed(2);
            if (scorePreviewEl) scorePreviewEl.textContent = finalScore;

            let label = "Pending";
            let badgeClass = "bg-secondary";

            const num = parseFloat(finalScore);
            if (num >= 4.50) {
                label = "Outstanding";
                badgeClass = "bg-success";
            } else if (num >= 3.50) {
                label = "Exceeds Expectations";
                badgeClass = "bg-primary";
            } else if (num >= 2.50) {
                label = "Meets Expectations";
                badgeClass = "bg-info text-dark";
            } else if (num >= 1.50) {
                label = "Needs Improvement";
                badgeClass = "bg-warning text-dark";
            } else {
                label = "Unsatisfactory";
                badgeClass = "bg-danger";
            }

            if (ratingLabelEl) ratingLabelEl.textContent = label;
            if (ratingBadgeEl) {
                ratingBadgeEl.className = "badge " + badgeClass + " p-2 fs-6";
                ratingBadgeEl.textContent = label;
            }
        }
    }

    scoreInputs.forEach(function (radio) {
        radio.addEventListener("change", recalculate);
    });

    // Run initial calculation for preloaded scores
    recalculate();
}
