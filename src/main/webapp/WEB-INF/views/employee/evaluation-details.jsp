<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Appraisal Report - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="history" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/employee/history" class="text-decoration-none small mb-1 d-inline-block">
                    &larr; Back to History
                </a>
                <h3 class="fw-bold mb-1">Performance Appraisal Report</h3>
                <p class="text-muted mb-0">Official review record for <strong>${evaluation.cycleName}</strong>.</p>
            </div>
            <div>
                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print / PDF
                </button>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Header Score Card -->
        <div class="card mb-4 border-0 shadow-sm bg-white">
            <div class="card-body p-4">
                <div class="row g-4 align-items-center">
                    <div class="col-md-7">
                        <span class="badge bg-primary-subtle text-primary mb-2">${evaluation.cycleName}</span>
                        <h4 class="fw-bold text-dark mb-1">${evaluation.employeeName}</h4>
                        <div class="text-muted mb-2">${evaluation.employeeTitle} &bull; ${evaluation.departmentName}</div>
                        <div class="small text-muted">
                            Evaluator: <strong>${evaluation.managerName}</strong> &bull; Submitted: <strong>${evaluation.submittedAt}</strong>
                        </div>
                    </div>
                    <div class="col-md-5 text-md-end">
                        <div class="d-inline-block p-3 rounded-4 bg-light border text-center">
                            <div class="text-muted small text-uppercase fw-bold">Overall Rating Score</div>
                            <div class="display-4 fw-bold text-primary my-1">${evaluation.totalScore}</div>
                            <span class="badge ${evaluation.ratingBadgeClass} fs-6 px-3 py-1">
                                ${evaluation.ratingLabel}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Radar Chart & Score Overview Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-header bg-white fw-bold">
                        <i class="bi bi-bullseye me-2 text-primary"></i>Competency Radar Analysis
                    </div>
                    <div class="card-body">
                        <div style="height: 300px;">
                            <canvas id="competencyRadarChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-header bg-white fw-bold">
                        <i class="bi bi-chat-left-quote me-2 text-primary"></i>Manager Feedback Highlights
                    </div>
                    <div class="card-body d-flex flex-column gap-3">
                        <div class="p-3 rounded-3 bg-success-subtle">
                            <div class="fw-bold text-success mb-1"><i class="bi bi-hand-thumbs-up-fill me-1"></i>Key Strengths</div>
                            <div class="small text-dark">${not empty evaluation.strengths ? evaluation.strengths : "Consistent execution across core competencies."}</div>
                        </div>

                        <div class="p-3 rounded-3 bg-warning-subtle">
                            <div class="fw-bold text-warning-emphasis mb-1"><i class="bi bi-lightbulb-fill me-1"></i>Development Goals & Improvements</div>
                            <div class="small text-dark">${not empty evaluation.improvements ? evaluation.improvements : "Maintain progress towards professional growth goals."}</div>
                        </div>

                        <div class="p-3 rounded-3 bg-light border">
                            <div class="fw-bold text-secondary mb-1"><i class="bi bi-chat-text-fill me-1"></i>Executive Remarks</div>
                            <div class="small text-dark">${not empty evaluation.overallFeedback ? evaluation.overallFeedback : "Good contribution during this cycle."}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Criteria Scores & Specific Feedback Table -->
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-list-check me-2 text-primary"></i>Individual Criteria Scores & Evaluator Notes
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Performance Dimension</th>
                            <th>Weightage</th>
                            <th>Score (1-5)</th>
                            <th>Evaluator Feedback</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sc" items="${evaluation.scores}">
                            <tr>
                                <td class="fw-semibold text-dark" style="width: 220px;">${sc.criterionName}</td>
                                <td style="width: 120px;">
                                    <span class="badge bg-secondary-subtle text-secondary">${sc.criterionWeight}%</span>
                                </td>
                                <td style="width: 140px;">
                                    <span class="score-pill ${sc.score >= 4 ? 'bg-success text-white' : (sc.score >= 3 ? 'bg-primary text-white' : 'bg-warning text-dark')}">
                                        ${sc.score}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty sc.comments}">
                                            <span>${sc.comments}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">No specific comments recorded.</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const radarCtx = document.getElementById('competencyRadarChart');
    if (radarCtx) {
        const labels = [
            <c:forEach var="sc" items="${evaluation.scores}" varStatus="status">
                "${sc.criterionName}"${!status.last ? ',' : ''}
            </c:forEach>
        ];
        const scores = [
            <c:forEach var="sc" items="${evaluation.scores}" varStatus="status">
                ${sc.score}${!status.last ? ',' : ''}
            </c:forEach>
        ];

        new Chart(radarCtx, {
            type: 'radar',
            data: {
                labels: labels.length > 0 ? labels : ['Attendance', 'Quality', 'Productivity', 'Teamwork', 'Communication', 'Goals'],
                datasets: [{
                    label: 'Score (1 - 5)',
                    data: scores.length > 0 ? scores : [4, 4, 5, 4, 4, 5],
                    backgroundColor: 'rgba(37, 99, 235, 0.2)',
                    borderColor: '#2563eb',
                    pointBackgroundColor: '#2563eb',
                    pointHoverBorderColor: '#2563eb'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    r: {
                        min: 0,
                        max: 5,
                        ticks: { stepSize: 1 }
                    }
                }
            }
        });
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
