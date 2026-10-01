<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="My Team Appraisals - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="team" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">My Team Appraisals</h3>
                <p class="text-muted mb-0">
                    <c:choose>
                        <c:when test="${not empty activeCycle}">
                            Active Review Window: <strong>${activeCycle.name}</strong> (${activeCycle.startDate} to ${activeCycle.endDate})
                        </c:when>
                        <c:otherwise>
                            No evaluation cycle is currently open for submissions.
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-people-fill me-2"></i>Assigned Direct Reports (${team.size()})
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Employee</th>
                            <th>Job Title</th>
                            <th>Department</th>
                            <th>Appraisal Status</th>
                            <th>Score</th>
                            <th>Rating Band</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="member" items="${team}">
                            <c:set var="ev" value="${evalMap[member.employeeId]}" />
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center gap-2">
                                        <div class="rounded-circle bg-primary-subtle text-primary fw-bold d-flex align-items-center justify-content-center" style="width: 38px; height: 38px;">
                                            ${member.fullName.substring(0, 1)}
                                        </div>
                                        <div>
                                            <div class="fw-semibold text-dark">${member.fullName}</div>
                                            <div class="small text-muted">${member.email}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>${member.jobTitle}</td>
                                <td><span class="badge bg-light text-dark border">${member.departmentName}</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty ev && ev.submitted}">
                                            <span class="badge bg-success-subtle text-success fs-6 px-3 py-1 rounded-pill">
                                                <i class="bi bi-check2-circle me-1"></i> Submitted
                                            </span>
                                        </c:when>
                                        <c:when test="${not empty ev && ev.draft}">
                                            <span class="badge bg-warning-subtle text-warning fs-6 px-3 py-1 rounded-pill">
                                                <i class="bi bi-hourglass-split me-1"></i> Draft Saved
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary-subtle text-secondary fs-6 px-3 py-1 rounded-pill">
                                                Not Started
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty ev && ev.totalScore != null}">
                                            <span class="fw-bold fs-6 text-primary">${ev.totalScore}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">-</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty ev && not empty ev.ratingLabel}">
                                            <span class="badge ${ev.ratingBadgeClass}">
                                                ${ev.ratingLabel}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">Pending</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:if test="${not empty activeCycle}">
                                        <c:choose>
                                            <c:when test="${not empty ev && ev.submitted}">
                                                <a href="${pageContext.request.contextPath}/manager/evaluation-view?id=${ev.id}" 
                                                   class="btn btn-sm btn-outline-secondary me-1">
                                                    <i class="bi bi-eye"></i> View
                                                </a>
                                                <a href="${pageContext.request.contextPath}/manager/evaluate?employeeId=${member.employeeId}" 
                                                   class="btn btn-sm btn-outline-primary">
                                                    <i class="bi bi-pencil"></i> Edit
                                                </a>
                                            </c:when>
                                            <c:when test="${not empty ev && ev.draft}">
                                                <a href="${pageContext.request.contextPath}/manager/evaluate?employeeId=${member.employeeId}" 
                                                   class="btn btn-sm btn-warning">
                                                    <i class="bi bi-pencil-square me-1"></i> Resume Draft
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/manager/evaluate?employeeId=${member.employeeId}" 
                                                   class="btn btn-sm btn-primary">
                                                    <i class="bi bi-clipboard2-plus me-1"></i> Start Evaluation
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty team}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    No direct reports currently assigned to you.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="../common/footer.jsp" />
