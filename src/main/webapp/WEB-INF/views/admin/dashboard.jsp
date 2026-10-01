<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Admin Dashboard - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="dashboard" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Organization Dashboard</h3>
                <p class="text-muted mb-0">Overview of employees, active appraisal cycles, and performance metrics.</p>
            </div>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/admin/cycles" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-calendar-plus me-1"></i> Manage Cycles
                </a>
                <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-primary btn-sm">
                    <i class="bi bi-bar-chart me-1"></i> Full Reports
                </a>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- KPI Cards Row -->
        <div class="row g-3 mb-4">
            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Total Staff</span>
                            <h3 class="fw-bold mt-1 mb-0">${stats.totalEmployees}</h3>
                        </div>
                        <div class="stat-icon bg-primary-subtle text-primary">
                            <i class="bi bi-people-fill"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">
                        Across ${stats.totalDepartments} departments
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Active Cycle</span>
                            <h5 class="fw-bold mt-1 mb-0 text-truncate" style="max-width: 170px;">
                                ${stats.activeCycle != null ? stats.activeCycle.name : "None Active"}
                            </h5>
                        </div>
                        <div class="stat-icon bg-success-subtle text-success">
                            <i class="bi bi-calendar-check-fill"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">
                        <c:choose>
                            <c:when test="${stats.activeCycle != null}">
                                <span class="badge bg-success">ACTIVE</span> ends ${stats.activeCycle.endDate}
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">No Cycle Open</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Completed Appraisals</span>
                            <h3 class="fw-bold mt-1 mb-0">${stats.completedEvaluations}</h3>
                        </div>
                        <div class="stat-icon bg-info-subtle text-info">
                            <i class="bi bi-clipboard2-check-fill"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">
                        ${stats.pendingEvaluations} evaluations pending
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Company Avg Score</span>
                            <h3 class="fw-bold mt-1 mb-0 text-primary">${stats.averageCompanyScore} <span class="fs-6 text-muted">/ 5.0</span></h3>
                        </div>
                        <div class="stat-icon bg-warning-subtle text-warning">
                            <i class="bi bi-star-fill"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">
                        Weighted company performance
                    </div>
                </div>
            </div>
        </div>

        <!-- Department Performance Overview -->
        <div class="row g-4 mb-4">
            <div class="col-lg-8">
                <div class="card h-100">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span class="fw-bold"><i class="bi bi-building me-2"></i>Department Performance Comparison</span>
                        <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none small">View Detailed Report &rarr;</a>
                    </div>
                    <div class="card-body">
                        <div style="height: 280px;">
                            <canvas id="deptPerformanceChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card h-100">
                    <div class="card-header fw-bold">
                        <i class="bi bi-lightning-fill text-warning me-2"></i>Quick Actions
                    </div>
                    <div class="card-body d-flex flex-column gap-2 justify-content-center">
                        <a href="${pageContext.request.contextPath}/admin/employees" class="btn btn-outline-primary d-flex align-items-center justify-content-between p-3">
                            <span class="d-flex align-items-center gap-2">
                                <i class="bi bi-person-plus-fill fs-5"></i>
                                <span>Register New Employee</span>
                            </span>
                            <i class="bi bi-chevron-right"></i>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/cycles" class="btn btn-outline-secondary d-flex align-items-center justify-content-between p-3">
                            <span class="d-flex align-items-center gap-2">
                                <i class="bi bi-calendar-event-fill fs-5"></i>
                                <span>Create / Manage Cycles</span>
                            </span>
                            <i class="bi bi-chevron-right"></i>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/criteria" class="btn btn-outline-info d-flex align-items-center justify-content-between p-3">
                            <span class="d-flex align-items-center gap-2">
                                <i class="bi bi-sliders fs-5"></i>
                                <span>Configure Criteria Weights</span>
                            </span>
                            <i class="bi bi-chevron-right"></i>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-outline-success d-flex align-items-center justify-content-between p-3">
                            <span class="d-flex align-items-center gap-2">
                                <i class="bi bi-file-earmark-spreadsheet-fill fs-5"></i>
                                <span>Export Performance CSV</span>
                            </span>
                            <i class="bi bi-chevron-right"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Department Table Summary -->
        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-table me-2"></i>Department Summary Breakdown
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Department</th>
                            <th>Total Employees</th>
                            <th>Completed Appraisals</th>
                            <th>Pending</th>
                            <th>Avg Score</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dept" items="${deptStats}">
                            <tr>
                                <td class="fw-semibold">${dept.departmentName}</td>
                                <td>${dept.totalEmployees}</td>
                                <td><span class="badge bg-success-subtle text-success">${dept.completedEvaluations}</span></td>
                                <td><span class="badge bg-warning-subtle text-warning">${dept.pendingEvaluations}</span></td>
                                <td>
                                    <span class="fw-bold">${dept.averageScore}</span> / 5.0
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/reports?departmentId=${dept.departmentId}" class="btn btn-sm btn-outline-primary">
                                        View Evaluations
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty deptStats}">
                            <tr>
                                <td colspan="6" class="text-center py-4 text-muted">No department statistics available for current cycle.</td>
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
    const ctx = document.getElementById('deptPerformanceChart');
    if (ctx) {
        const labels = [
            <c:forEach var="d" items="${deptStats}" varStatus="status">
                "${d.departmentName}"${!status.last ? ',' : ''}
            </c:forEach>
        ];
        const scores = [
            <c:forEach var="d" items="${deptStats}" varStatus="status">
                ${d.averageScore}${!status.last ? ',' : ''}
            </c:forEach>
        ];

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels.length > 0 ? labels : ['Engineering', 'Sales & Marketing', 'Human Resources'],
                datasets: [{
                    label: 'Average Performance Score (1 - 5)',
                    data: scores.length > 0 ? scores : [4.15, 3.65, 4.30],
                    backgroundColor: ['#2563eb', '#06b6d4', '#10b981'],
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
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
