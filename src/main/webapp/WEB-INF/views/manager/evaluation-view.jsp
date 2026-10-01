<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Evaluation Appraisal - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="team" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="javascript:history.back()" class="text-decoration-none small mb-1 d-inline-block">
                    &larr; Back
                </a>
                <h3 class="fw-bold mb-1">Performance Appraisal Record</h3>
                <p class="text-muted mb-0">Official evaluation appraisal record for <strong>${evaluation.employeeName}</strong>.</p>
            </div>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print / PDF
                </button>
                <a href="${pageContext.request.contextPath}/manager/evaluate?employeeId=${evaluation.employeeId}" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-pencil me-1"></i> Edit Appraisal
                </a>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Header Card -->
        <div class="card mb-4 border-0 shadow-sm bg-white">
            <div class="card-body p-4">
                <div class="row g-4 align-items-center">
                    <div class="col-md-7">
                        <span class="badge bg-primary-subtle text-primary mb-2">Cycle: ${evaluation.cycleName}</span>
                        <h4 class="fw-bold text-dark mb-1">${evaluation.employeeName}</h4>
                        <div class="text-muted mb-2">${evaluation.employeeTitle} &bull; ${evaluation.departmentName}</div>
                        <div class="small text-muted">
                            Evaluated by: <strong>${evaluation.managerName}</strong> &bull; Submitted: <strong>${evaluation.submittedAt}</strong>
                        </div>
                    </div>
                    <div class="col-md-5 text-md-end">
                        <div class="d-inline-block p-3 rounded-4 bg-light border text-center">
                            <div class="text-muted small text-uppercase fw-bold">Overall Rating</div>
                            <div class="display-5 fw-bold text-primary my-1">${evaluation.totalScore}</div>
                            <span class="badge ${evaluation.ratingBadgeClass} fs-6 px-3 py-1">
                                ${evaluation.ratingLabel}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Scores Breakdown Table -->
        <div class="card mb-4">
            <div class="card-header fw-bold">
                <i class="bi bi-check2-all me-2"></i>Performance Criteria Scores & Feedback
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Dimension</th>
                            <th>Weightage</th>
                            <th>Score (1-5)</th>
                            <th>Manager Feedback & Observations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sc" items="${evaluation.scores}">
                            <tr>
                                <td class="fw-semibold text-dark" style="width: 220px;">
                                    ${sc.criterionName}
                                </td>
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

        <!-- Qualitative Summary Feedback -->
        <div class="row g-4 mb-4">
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header fw-bold text-success">
                        <i class="bi bi-hand-thumbs-up me-2"></i>Key Strengths
                    </div>
                    <div class="card-body">
                        <p class="mb-0 text-secondary">
                            ${not empty evaluation.strengths ? evaluation.strengths : "No specific strengths highlighted."}
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header fw-bold text-warning">
                        <i class="bi bi-lightbulb me-2"></i>Areas for Development & Improvement
                    </div>
                    <div class="card-body">
                        <p class="mb-0 text-secondary">
                            ${not empty evaluation.improvements ? evaluation.improvements : "No specific development areas noted."}
                        </p>
                    </div>
                </div>
            </div>

            <div class="col-12">
                <div class="card">
                    <div class="card-header fw-bold">
                        <i class="bi bi-chat-quote me-2"></i>Executive Summary Feedback
                    </div>
                    <div class="card-body">
                        <p class="mb-0 text-dark">
                            ${not empty evaluation.overallFeedback ? evaluation.overallFeedback : "No overall summary provided."}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="../common/footer.jsp" />
