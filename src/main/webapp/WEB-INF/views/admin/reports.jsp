<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Performance Reports - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="reports" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Organization Performance Reports</h3>
                <p class="text-muted mb-0">Cross-departmental performance metrics, score distributions, and exportable analytics.</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/reports/export?cycleId=${selectedCycleId}&departmentId=${selectedDeptId}&ratingLabel=${selectedRating}&status=${selectedStatus}" 
                   class="btn btn-success btn-sm shadow-sm">
                    <i class="bi bi-file-earmark-arrow-down-fill me-1"></i> Export to CSV
                </a>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Filter Bar Card -->
        <div class="card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/reports" method="get" class="row g-3 align-items-end">
                    <div class="col-md-3">
                        <label class="form-label fw-semibold small text-uppercase">Evaluation Cycle</label>
                        <select name="cycleId" class="form-select">
                            <c:forEach var="c" items="${cycles}">
                                <option value="${c.id}" ${c.id == selectedCycleId ? 'selected' : ''}>${c.name} (${c.status})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold small text-uppercase">Department</label>
                        <select name="departmentId" class="form-select">
                            <option value="">-- All Departments --</option>
                            <c:forEach var="d" items="${departments}">
                                <option value="${d.id}" ${d.id == selectedDeptId ? 'selected' : ''}>${d.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold small text-uppercase">Performance Rating</label>
                        <select name="ratingLabel" class="form-select">
                            <option value="ALL">-- All Rating Bands --</option>
                            <option value="Outstanding" ${selectedRating == 'Outstanding' ? 'selected' : ''}>Outstanding (4.5 - 5.0)</option>
                            <option value="Exceeds Expectations" ${selectedRating == 'Exceeds Expectations' ? 'selected' : ''}>Exceeds Expectations (3.5 - 4.49)</option>
                            <option value="Meets Expectations" ${selectedRating == 'Meets Expectations' ? 'selected' : ''}>Meets Expectations (2.5 - 3.49)</option>
                            <option value="Needs Improvement" ${selectedRating == 'Needs Improvement' ? 'selected' : ''}>Needs Improvement (1.5 - 2.49)</option>
                            <option value="Unsatisfactory" ${selectedRating == 'Unsatisfactory' ? 'selected' : ''}>Unsatisfactory (Below 1.5)</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label class="form-label fw-semibold small text-uppercase">Status</label>
                        <select name="status" class="form-select">
                            <option value="ALL">-- All Statuses --</option>
                            <option value="SUBMITTED" ${selectedStatus == 'SUBMITTED' ? 'selected' : ''}>Completed / Submitted</option>
                            <option value="DRAFT" ${selectedStatus == 'DRAFT' ? 'selected' : ''}>Draft / In Progress</option>
                        </select>
                    </div>

                    <div class="col-md-1 d-grid">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-filter"></i> Apply
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Chart Analytics Section -->
        <div class="row g-4 mb-4">
            <div class="col-lg-7">
                <div class="card h-100">
                    <div class="card-header fw-bold">
                        <i class="bi bi-bar-chart-fill me-2 text-primary"></i>Department Average Score Comparison
                    </div>
                    <div class="card-body">
                        <div style="height: 260px;">
                            <canvas id="deptBarChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-5">
                <div class="card h-100">
                    <div class="card-header fw-bold">
                        <i class="bi bi-pie-chart-fill me-2 text-info"></i>Rating Band Distribution
                    </div>
                    <div class="card-body">
                        <div style="height: 260px;">
                            <canvas id="ratingDoughnutChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filtered Appraisals Table -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <span class="fw-bold"><i class="bi bi-table me-2"></i>Performance Evaluations (${evaluations.size()} records)</span>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Employee</th>
                            <th>Department</th>
                            <th>Evaluator (Manager)</th>
                            <th>Cycle</th>
                            <th>Total Score</th>
                            <th>Rating Band</th>
                            <th>Status</th>
                            <th>Submitted Date</th>
                            <th class="text-end">Details</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ev" items="${evaluations}">
                            <tr>
                                <td>
                                    <div class="fw-semibold text-dark">${ev.employeeName}</div>
                                    <div class="small text-muted">${ev.employeeTitle}</div>
                                </td>
                                <td><span class="badge bg-light text-dark border">${ev.departmentName}</span></td>
                                <td><i class="bi bi-person text-secondary me-1"></i>${ev.managerName}</td>
                                <td>${ev.cycleName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ev.totalScore != null}">
                                            <span class="fw-bold fs-6 text-primary">${ev.totalScore}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">Pending</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge ${ev.ratingBadgeClass}">
                                        ${ev.ratingLabel != null ? ev.ratingLabel : 'Pending'}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${ev.statusBadgeClass}">
                                        ${ev.status}
                                    </span>
                                </td>
                                <td class="small text-muted">${ev.submittedAt != null ? ev.submittedAt : '-'}</td>
                                <td class="text-end">
                                    <c:if test="${ev.status != 'DRAFT' && ev.totalScore != null}">
                                        <a href="${pageContext.request.contextPath}/manager/evaluation-view?id=${ev.id}" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye"></i> View
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty evaluations}">
                            <tr>
                                <td colspan="9" class="text-center py-5 text-muted">
                                    <i class="bi bi-folder-x fs-1 d-block mb-2"></i>
                                    No evaluations match the selected filter criteria.
                                </td>
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
    // 1. Department Bar Chart
    const deptCtx = document.getElementById('deptBarChart');
    if (deptCtx) {
        const deptLabels = [
            <c:forEach var="d" items="${deptStats}" varStatus="status">
                "${d.departmentName}"${!status.last ? ',' : ''}
            </c:forEach>
        ];
        const deptAverages = [
            <c:forEach var="d" items="${deptStats}" varStatus="status">
                ${d.averageScore}${!status.last ? ',' : ''}
            </c:forEach>
        ];

        new Chart(deptCtx, {
            type: 'bar',
            data: {
                labels: deptLabels.length > 0 ? deptLabels : ['Engineering', 'Sales & Marketing', 'Human Resources'],
                datasets: [{
                    label: 'Department Average Score',
                    data: deptAverages.length > 0 ? deptAverages : [4.15, 3.65, 4.30],
                    backgroundColor: '#2563eb',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: { beginAtZero: true, max: 5 }
                }
            }
        });
    }

    // 2. Rating Doughnut Chart
    const ratingCtx = document.getElementById('ratingDoughnutChart');
    if (ratingCtx) {
        // Aggregate totals across deptStats
        let totalOutstanding = 0;
        let totalExceeds = 0;
        let totalMeets = 0;
        let totalNeeds = 0;
        let totalUnsat = 0;

        <c:forEach var="d" items="${deptStats}">
            totalOutstanding += ${d.outstandingCount};
            totalExceeds += ${d.exceedsCount};
            totalMeets += ${d.meetsCount};
            totalNeeds += ${d.needsImprovementCount};
            totalUnsat += ${d.unsatisfactoryCount};
        </c:forEach>

        new Chart(ratingCtx, {
            type: 'doughnut',
            data: {
                labels: ['Outstanding (4.5-5.0)', 'Exceeds (3.5-4.49)', 'Meets (2.5-3.49)', 'Needs Impr (1.5-2.49)', 'Unsatisfactory (<1.5)'],
                datasets: [{
                    data: [totalOutstanding, totalExceeds, totalMeets, totalNeeds, totalUnsat],
                    backgroundColor: ['#198754', '#0d6efd', '#0dcaf0', '#ffc107', '#dc3545']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
