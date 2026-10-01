<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Appraise Employee - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="team" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/manager/team" class="text-decoration-none small mb-1 d-inline-block">
                    &larr; Back to Team List
                </a>
                <h3 class="fw-bold mb-1">Performance Appraisal Form</h3>
                <p class="text-muted mb-0">
                    Evaluating <strong>${employee.fullName}</strong> (${employee.jobTitle}) for <strong>${cycle.name}</strong>.
                </p>
            </div>
            <div>
                <span class="badge ${cycle.statusBadgeClass} fs-6 px-3 py-2">
                    ${cycle.name} &bull; ${cycle.status}
                </span>
            </div>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <form action="${pageContext.request.contextPath}/manager/evaluate" method="post" id="appraisalForm">
            <input type="hidden" name="employeeId" value="${employee.employeeId}" />
            <input type="hidden" name="cycleId" value="${cycle.id}" />
            <input type="hidden" name="evaluationId" value="${evaluation != null ? evaluation.id : ''}" />
            <input type="hidden" name="action" id="formAction" value="submit" />

            <div class="row g-4">
                <!-- Main Scoring Column -->
                <div class="col-lg-8">
                    <!-- Employee Profile Header Card -->
                    <div class="card mb-4 bg-light border">
                        <div class="card-body">
                            <div class="row g-3 align-items-center">
                                <div class="col-md-6">
                                    <h5 class="fw-bold text-dark mb-1">${employee.fullName}</h5>
                                    <div class="text-muted small">${employee.email} &bull; ${employee.jobTitle}</div>
                                </div>
                                <div class="col-md-6 text-md-end">
                                    <span class="badge bg-secondary-subtle text-secondary me-2">Dept: ${employee.departmentName}</span>
                                    <span class="badge bg-secondary-subtle text-secondary">Hired: ${employee.hireDate}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Evaluation Criteria Cards -->
                    <h5 class="fw-bold mb-3">
                        <i class="bi bi-star-half text-warning me-2"></i>Performance Criteria Evaluation (1 - 5)
                    </h5>

                    <c:forEach var="crit" items="${criteria}">
                        <c:set var="prevScoreObj" value="${scoreMap[crit.id]}" />
                        <c:set var="prevScore" value="${prevScoreObj != null ? prevScoreObj.score : 3}" />
                        <c:set var="prevComment" value="${prevScoreObj != null ? prevScoreObj.comments : ''}" />

                        <div class="criterion-card shadow-sm mb-3">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <div>
                                    <h6 class="fw-bold text-dark mb-1">
                                        <i class="bi bi-check-circle-fill text-primary me-1"></i>${crit.name}
                                    </h6>
                                    <p class="text-muted small mb-0">${crit.description}</p>
                                </div>
                                <span class="badge bg-primary-subtle text-primary fs-6 px-3 py-1 rounded-pill">
                                    Weight: ${crit.weight}%
                                </span>
                            </div>

                            <!-- 1-5 Score Radio Selector -->
                            <div class="my-3">
                                <label class="form-label small fw-semibold text-secondary mb-2">Select Score (1 - 5):</label>
                                <div class="score-selector">
                                    <c:forEach var="val" begin="1" end="5">
                                        <input type="radio" class="btn-check criterion-score-input" 
                                               name="score_${crit.id}" id="crit_${crit.id}_${val}" 
                                               value="${val}" data-criterion-id="${crit.id}" data-weight="${crit.weight}"
                                               ${prevScore == val ? 'checked' : ''} />
                                        <label class="score-btn-label" for="crit_${crit.id}_${val}">
                                            <div>${val}</div>
                                            <small class="d-block text-nowrap" style="font-size: 0.65rem;">
                                                <c:choose>
                                                    <c:when test="${val == 1}">Unsat.</c:when>
                                                    <c:when test="${val == 2}">Needs Imp.</c:when>
                                                    <c:when test="${val == 3}">Meets</c:when>
                                                    <c:when test="${val == 4}">Exceeds</c:when>
                                                    <c:when test="${val == 5}">Outst.</c:when>
                                                </c:choose>
                                            </small>
                                        </label>
                                    </c:forEach>
                                </div>
                            </div>

                            <!-- Criterion Specific Feedback Comments -->
                            <div class="mt-2">
                                <label class="form-label small fw-semibold text-secondary">Specific Feedback / Observations for ${crit.name}:</label>
                                <textarea name="comment_${crit.id}" class="form-control" rows="2" 
                                          placeholder="Specific examples, achievements, or areas where ${employee.fullName} can develop in this area...">${prevComment}</textarea>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Overall Qualitative Feedback -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header fw-bold">
                            <i class="bi bi-chat-left-quote me-2"></i>Overall Qualitative Assessment
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Key Strengths Demonstrated</label>
                                <textarea name="strengths" class="form-control" rows="3" 
                                          placeholder="Highlight standout accomplishments, notable initiatives, or exceptional behaviors...">${evaluation != null ? evaluation.strengths : ''}</textarea>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Areas for Improvement & Development Goals</label>
                                <textarea name="improvements" class="form-control" rows="3" 
                                          placeholder="Actionable recommendations, training opportunities, or specific goals for growth...">${evaluation != null ? evaluation.improvements : ''}</textarea>
                            </div>

                            <div class="mb-0">
                                <label class="form-label fw-semibold">Overall Summary Feedback</label>
                                <textarea name="overallFeedback" class="form-control" rows="3" 
                                          placeholder="Comprehensive summary of employee's overall contribution during this appraisal period...">${evaluation != null ? evaluation.overallFeedback : ''}</textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Sticky Summary & Live Calculation Sidebar -->
                <div class="col-lg-4">
                    <div class="sticky-top" style="top: 80px;">
                        <div class="card shadow-sm border-primary">
                            <div class="card-header bg-primary text-white fw-bold d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-calculator me-2"></i>Live Score Preview</span>
                                <span class="badge bg-white text-primary">Dynamic</span>
                            </div>
                            <div class="card-body text-center p-4">
                                <div class="text-muted small text-uppercase fw-semibold mb-1">Calculated Weighted Score</div>
                                <div class="display-3 fw-bold text-primary my-2" id="liveTotalScore">
                                    ${evaluation != null && evaluation.totalScore != null ? evaluation.totalScore : "3.00"}
                                </div>
                                <div class="text-muted small mb-3">out of 5.00 maximum</div>

                                <div class="mb-3">
                                    <span class="badge bg-primary p-2 fs-6" id="liveRatingBadge">
                                        ${evaluation != null && evaluation.ratingLabel != null ? evaluation.ratingLabel : "Meets Expectations"}
                                    </span>
                                </div>

                                <hr />

                                <!-- Rating Scale Legend -->
                                <div class="text-start small mb-4">
                                    <div class="fw-bold mb-2 text-dark">Official Rating Standards:</div>
                                    <div class="d-flex justify-content-between py-1 border-bottom">
                                        <span>4.50 – 5.00</span>
                                        <span class="badge bg-success">Outstanding</span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1 border-bottom">
                                        <span>3.50 – 4.49</span>
                                        <span class="badge bg-primary">Exceeds Expectations</span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1 border-bottom">
                                        <span>2.50 – 3.49</span>
                                        <span class="badge bg-info text-dark">Meets Expectations</span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1 border-bottom">
                                        <span>1.50 – 2.49</span>
                                        <span class="badge bg-warning text-dark">Needs Improvement</span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1">
                                        <span>Below 1.50</span>
                                        <span class="badge bg-danger">Unsatisfactory</span>
                                    </div>
                                </div>

                                <!-- Action Buttons -->
                                <div class="d-grid gap-2">
                                    <button type="button" class="btn btn-primary btn-lg fw-semibold" onclick="submitForm('submit')">
                                        <i class="bi bi-check-circle-fill me-1"></i> Submit Evaluation
                                    </button>
                                    <button type="button" class="btn btn-outline-secondary" onclick="submitForm('draft')">
                                        <i class="bi bi-save me-1"></i> Save as Draft
                                    </button>
                                    <a href="${pageContext.request.contextPath}/manager/team" class="btn btn-link text-muted btn-sm">
                                        Cancel and Return
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </main>
</div>

<script>
function submitForm(actionType) {
    document.getElementById('formAction').value = actionType;
    if (actionType === 'submit') {
        if (!confirm('Are you ready to submit this final performance evaluation?')) {
            return;
        }
    }
    document.getElementById('appraisalForm').submit();
}
</script>

<jsp:include page="../common/footer.jsp" />
