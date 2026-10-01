<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="My Evaluation History - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="history" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">My Performance Evaluation History</h3>
                <p class="text-muted mb-0">Review past appraisal scores, ratings, and manager development recommendations.</p>
            </div>
            <a href="${pageContext.request.contextPath}/employee/dashboard" class="btn btn-outline-secondary btn-sm">
                &larr; Back to Dashboard
            </a>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card shadow-sm border-0">
            <div class="card-header bg-white fw-bold">
                <i class="bi bi-clock-history me-2 text-primary"></i>Historical Appraisals (${history.size()})
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Evaluation Cycle</th>
                            <th>Evaluator (Manager)</th>
                            <th>Total Weighted Score</th>
                            <th>Rating Band</th>
                            <th>Status</th>
                            <th>Completed Date</th>
                            <th class="text-end">Appraisal Report</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ev" items="${history}">
                            <tr>
                                <td class="fw-semibold text-dark">${ev.cycleName}</td>
                                <td><i class="bi bi-person text-secondary me-1"></i>${ev.managerName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ev.totalScore != null}">
                                            <span class="fw-bold fs-6 text-primary">${ev.totalScore}</span> <small class="text-muted">/ 5.0</small>
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
                                <td><span class="badge ${ev.statusBadgeClass}">${ev.status}</span></td>
                                <td class="small text-muted">${ev.submittedAt != null ? ev.submittedAt : '-'}</td>
                                <td class="text-end">
                                    <c:if test="${ev.submitted}">
                                        <a href="${pageContext.request.contextPath}/employee/evaluation-details?id=${ev.id}" 
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-file-earmark-text me-1"></i> View Report
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty history}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="bi bi-folder2-open fs-1 d-block mb-2"></i>
                                    No historical performance evaluations found on record.
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
