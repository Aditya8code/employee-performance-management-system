<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Manager Dashboard - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="dashboard" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Manager Overview</h3>
                <p class="text-muted mb-0">Track and review appraisals for your assigned direct reports.</p>
            </div>
            <a href="${pageContext.request.contextPath}/manager/team" class="btn btn-primary btn-sm">
                <i class="bi bi-people-fill me-1"></i> My Team Appraisals
            </a>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Metric Cards -->
        <div class="row g-3 mb-4">
            <div class="col-sm-6 col-md-4">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Direct Reports</span>
                            <h3 class="fw-bold mt-1 mb-0">${team.size()}</h3>
                        </div>
                        <div class="stat-icon bg-primary-subtle text-primary">
                            <i class="bi bi-people-fill"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">Assigned team members</div>
                </div>
            </div>

            <div class="col-sm-6 col-md-4">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Pending Reviews</span>
                            <h3 class="fw-bold mt-1 mb-0 text-warning">${pendingCount}</h3>
                        </div>
                        <div class="stat-icon bg-warning-subtle text-warning">
                            <i class="bi bi-hourglass-split"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">Awaiting evaluation in active cycle</div>
                </div>
            </div>

            <div class="col-sm-6 col-md-4">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted small text-uppercase fw-bold">Submitted Reviews</span>
                            <h3 class="fw-bold mt-1 mb-0 text-success">${completedCount}</h3>
                        </div>
                        <div class="stat-icon bg-success-subtle text-success">
                            <i class="bi bi-check2-all"></i>
                        </div>
                    </div>
                    <div class="mt-2 text-muted small">Completed appraisals</div>
                </div>
            </div>
        </div>

        <!-- Active Cycle Alert Banner -->
        <c:choose>
            <c:when test="${not empty activeCycle}">
                <div class="alert alert-info d-flex justify-content-between align-items-center p-3 mb-4 rounded-3 shadow-sm border-info-subtle">
                    <div class="d-flex align-items-center gap-3">
                        <i class="bi bi-calendar2-check-fill fs-2 text-info"></i>
                        <div>
                            <h5 class="alert-heading mb-1 fw-bold">${activeCycle.name}</h5>
                            <p class="mb-0 small text-muted">
                                Active appraisal window: <strong>${activeCycle.startDate}</strong> to <strong>${activeCycle.endDate}</strong>.
                                Please ensure all direct reports are evaluated before the deadline.
                            </p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/manager/team" class="btn btn-primary btn-sm text-nowrap">
                        Conduct Appraisals &rarr;
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-secondary d-flex align-items-center gap-2 mb-4">
                    <i class="bi bi-info-circle-fill fs-5"></i>
                    <div>There is currently no active evaluation cycle open.</div>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Direct Reports Quick List -->
        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-person-lines-fill me-2"></i>My Direct Reports
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Employee Name</th>
                            <th>Job Title</th>
                            <th>Department</th>
                            <th>Hire Date</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="member" items="${team}">
                            <tr>
                                <td>
                                    <div class="fw-semibold text-dark">${member.fullName}</div>
                                    <div class="small text-muted">${member.email}</div>
                                </td>
                                <td>${member.jobTitle}</td>
                                <td><span class="badge bg-light text-dark border">${member.departmentName}</span></td>
                                <td>${member.hireDate}</td>
                                <td class="text-end">
                                    <c:if test="${not empty activeCycle}">
                                        <a href="${pageContext.request.contextPath}/manager/evaluate?employeeId=${member.employeeId}" 
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-pencil-square me-1"></i> Review Appraisal
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty team}">
                            <tr>
                                <td colspan="5" class="text-center py-4 text-muted">No direct reports currently assigned to you.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="../common/footer.jsp" />
