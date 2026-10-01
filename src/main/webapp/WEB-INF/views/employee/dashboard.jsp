<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Employee Dashboard - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="dashboard" />
    </jsp:include>

    <main class="main-content">
        <!-- Welcome Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Welcome, ${employee.fullName}!</h3>
                <p class="text-muted mb-0">Here is your personal performance appraisal portal and review history.</p>
            </div>
            <a href="${pageContext.request.contextPath}/employee/history" class="btn btn-outline-primary btn-sm">
                <i class="bi bi-clock-history me-1"></i> Full Appraisal History
            </a>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Profile & Score Highlights Row -->
        <div class="row g-4 mb-4">
            <!-- Profile Card -->
            <div class="col-md-5">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <div class="rounded-circle bg-primary text-white fw-bold d-flex align-items-center justify-content-center display-6" style="width: 64px; height: 64px;">
                                ${employee.fullName.substring(0, 1)}
                            </div>
                            <div>
                                <h5 class="fw-bold text-dark mb-0">${employee.fullName}</h5>
                                <div class="text-primary fw-medium small">${employee.jobTitle}</div>
                                <div class="text-muted small">${employee.email}</div>
                            </div>
                        </div>
                        <hr />
                        <div class="row g-2 small">
                            <div class="col-6">
                                <span class="text-muted d-block">Department:</span>
                                <span class="fw-semibold text-dark">${employee.departmentName}</span>
                            </div>
                            <div class="col-6">
                                <span class="text-muted d-block">Reporting Manager:</span>
                                <span class="fw-semibold text-dark">${not empty employee.managerName ? employee.managerName : "Unassigned"}</span>
                            </div>
                            <div class="col-6 mt-2">
                                <span class="text-muted d-block">Hire Date:</span>
                                <span class="fw-semibold text-dark">${employee.hireDate}</span>
                            </div>
                            <div class="col-6 mt-2">
                                <span class="text-muted d-block">Account Status:</span>
                                <span class="badge bg-success-subtle text-success">${employee.status}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Latest Score Card -->
            <div class="col-md-7">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body p-4 d-flex flex-column justify-content-between">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Latest Appraised Performance</span>
                            <c:choose>
                                <c:when test="${not empty latestCompletedEval}">
                                    <div class="d-flex align-items-baseline gap-3 my-2">
                                        <div class="display-3 fw-bold text-primary">${latestCompletedEval.totalScore}</div>
                                        <div>
                                            <div class="fs-5 text-muted">/ 5.00</div>
                                            <span class="badge ${latestCompletedEval.ratingBadgeClass} fs-6 px-3 py-1">
                                                ${latestCompletedEval.ratingLabel}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="small text-muted mb-2">
                                        From <strong>${latestCompletedEval.cycleName}</strong> &bull; Evaluated by <strong>${latestCompletedEval.managerName}</strong>
                                    </div>
                                    <p class="text-secondary small fst-italic mb-0">
                                        "${not empty latestCompletedEval.overallFeedback ? latestCompletedEval.overallFeedback : 'Consistently solid performance.'}"
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <div class="py-4 text-center text-muted">
                                        <i class="bi bi-clipboard-x fs-1 d-block mb-2"></i>
                                        No finalized performance appraisal on record yet.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:if test="${not empty latestCompletedEval}">
                            <div class="mt-3 text-end">
                                <a href="${pageContext.request.contextPath}/employee/evaluation-details?id=${latestCompletedEval.id}" 
                                   class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-file-earmark-text me-1"></i> View Detailed Breakdown &rarr;
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Performance History Over Time Chart -->
        <c:if test="${not empty history}">
            <div class="card mb-4 shadow-sm border-0">
                <div class="card-header bg-white fw-bold">
                    <i class="bi bi-graph-up me-2 text-primary"></i>My Performance Score History Over Review Cycles
                </div>
                <div class="card-body">
                    <div style="height: 240px;">
                        <canvas id="employeeHistoryChart"></canvas>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Evaluation History Table -->
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-journal-text me-2"></i>Recent Appraisals
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Review Cycle</th>
                            <th>Evaluator</th>
                            <th>Overall Score</th>
                            <th>Rating Band</th>
                            <th>Status</th>
                            <th>Date</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ev" items="${history}">
                            <tr>
                                <td class="fw-semibold text-dark">${ev.cycleName}</td>
                                <td>${ev.managerName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ev.totalScore != null}">
                                            <span class="fw-bold fs-6 text-primary">${ev.totalScore}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">-</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge ${ev.ratingBadgeClass}">
                                        ${ev.ratingLabel != null ? ev.ratingLabel : 'Pending'}
                                    </span>
                                </td>
                                <td><span class="badge ${ev.statusBadgeClass}">${ev.status}</span></td>
                                <td class="small text-muted">${ev.submittedAt != null ? ev.submittedAt : '-'}</td>
                                <td class="text-end">
                                    <c:if test="${ev.submitted}">
                                        <a href="${pageContext.request.contextPath}/employee/evaluation-details?id=${ev.id}" 
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye"></i> View Details
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty history}">
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">No evaluations recorded yet.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('employeeHistoryChart');
    if (ctx) {
        const cycleLabels = [
            <c:forEach var="h" items="${history}" varStatus="status">
                <c:if test="${h.submitted && h.totalScore != null}">
                    "${h.cycleName}"${!status.last ? ',' : ''}
                </c:if>
            </c:forEach>
        ];
        const cycleScores = [
            <c:forEach var="h" items="${history}" varStatus="status">
                <c:if test="${h.submitted && h.totalScore != null}">
                    ${h.totalScore}${!status.last ? ',' : ''}
                </c:if>
            </c:forEach>
        ];

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: cycleLabels.length > 0 ? cycleLabels : ['Past Review'],
                datasets: [{
                    label: 'Performance Score',
                    data: cycleScores.length > 0 ? cycleScores : [4.0],
                    borderColor: '#2563eb',
                    backgroundColor: 'rgba(37, 99, 235, 0.1)',
                    fill: true,
                    tension: 0.3,
                    pointRadius: 6,
                    pointBackgroundColor: '#2563eb'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: { beginAtZero: false, min: 1, max: 5 }
                }
            }
        });
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
