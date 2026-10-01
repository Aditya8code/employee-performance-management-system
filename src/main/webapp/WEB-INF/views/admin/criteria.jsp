<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Evaluation Criteria - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="criteria" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Evaluation Criteria & Weightages</h3>
                <p class="text-muted mb-0">Configure appraisal scoring dimensions, descriptions, and relative weight contributions.</p>
            </div>
            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addCriterionModal">
                <i class="bi bi-plus-lg me-1"></i> Add Criterion
            </button>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <!-- Total Weight Banner -->
        <div class="card mb-4 bg-light border">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="fw-bold text-dark">
                        <i class="bi bi-pie-chart-fill text-primary me-2"></i>Total Active Criteria Weight
                    </span>
                    <span class="badge ${totalWeight == 100.00 ? 'bg-success' : 'bg-warning text-dark'} fs-6 px-3 py-2">
                        ${totalWeight}% / 100%
                    </span>
                </div>
                <div class="progress" style="height: 10px;">
                    <div class="progress-bar ${totalWeight == 100.00 ? 'bg-success' : 'bg-warning'}" 
                         role="progressbar" style="width: ${totalWeight}%;" aria-valuenow="${totalWeight}" aria-valuemin="0" aria-valuemax="100">
                    </div>
                </div>
                <small class="text-muted mt-1 d-block">
                    <c:choose>
                        <c:when test="${totalWeight == 100.00}">
                            <i class="bi bi-check-circle-fill text-success me-1"></i> Perfect! Active weights sum exactly to 100%.
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-exclamation-triangle-fill text-warning me-1"></i> Notice: Total active weight is ${totalWeight}%. Recommended sum is 100%.
                        </c:otherwise>
                    </c:choose>
                </small>
            </div>
        </div>

        <!-- Criteria Table -->
        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-list-check me-2"></i>Configured Performance Dimensions (${criteria.size()})
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Criterion Name</th>
                            <th>Assessment Description</th>
                            <th>Weightage</th>
                            <th>Active Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cr" items="${criteria}">
                            <tr>
                                <td>${cr.id}</td>
                                <td class="fw-bold text-dark">
                                    <i class="bi bi-bookmark-check text-primary me-1"></i>${cr.name}
                                </td>
                                <td class="text-muted text-truncate" style="max-width: 380px;">${cr.description}</td>
                                <td>
                                    <span class="badge bg-primary-subtle text-primary fs-6 px-3 py-1 rounded-pill">
                                        ${cr.weight}%
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${cr.active ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}">
                                        ${cr.active ? 'ACTIVE' : 'INACTIVE'}
                                    </span>
                                </td>
                                <td class="text-end">
                                    <button class="btn btn-sm btn-outline-secondary me-1"
                                            onclick="openEditCriterionModal(${cr.id}, '${cr.name}', '${cr.description}', '${cr.weight}', ${cr.active})">
                                        <i class="bi bi-pencil"></i> Edit
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="openDeleteCriterionModal(${cr.id}, '${cr.name}')">
                                        <i class="bi bi-trash"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Modal: Add Criterion -->
<div class="modal fade" id="addCriterionModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/criteria" method="post" class="modal-content">
            <input type="hidden" name="action" value="create" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Add Evaluation Criterion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Criterion Name</label>
                    <input type="text" name="name" class="form-control" placeholder="e.g. Leadership & Initiative" required />
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Weightage Percentage (%)</label>
                    <input type="number" step="0.01" min="1" max="100" name="weight" class="form-control" placeholder="e.g. 15.00" required />
                    <div class="form-text">Specify weight between 1% and 100%.</div>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Assessment Guidelines / Description</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Provide clarity on how managers should evaluate this dimension..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Criterion</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit Criterion -->
<div class="modal fade" id="editCriterionModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/criteria" method="post" class="modal-content">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" id="editCritId" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Edit Evaluation Criterion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Criterion Name</label>
                    <input type="text" name="name" id="editCritName" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Weightage Percentage (%)</label>
                    <input type="number" step="0.01" min="1" max="100" name="weight" id="editCritWeight" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Assessment Guidelines / Description</label>
                    <textarea name="description" id="editCritDesc" class="form-control" rows="3"></textarea>
                </div>
                <div class="form-check form-switch mb-2">
                    <input class="form-check-input" type="checkbox" name="active" value="true" id="editCritActive">
                    <label class="form-check-label fw-semibold" for="editCritActive">Active in Evaluations</label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Criterion</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Delete Criterion -->
<div class="modal fade" id="deleteCriterionModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/criteria" method="post" class="modal-content">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="id" id="delCritId" />
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title fw-bold">Confirm Deletion</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete criterion <strong id="delCritName"></strong>?</p>
                <div class="alert alert-warning small">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    Existing evaluations recorded with this criterion may restrict deletion.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-danger">Delete Criterion</button>
            </div>
        </form>
    </div>
</div>

<script>
function openEditCriterionModal(id, name, desc, weight, active) {
    document.getElementById('editCritId').value = id;
    document.getElementById('editCritName').value = name;
    document.getElementById('editCritDesc').value = desc || '';
    document.getElementById('editCritWeight').value = weight;
    document.getElementById('editCritActive').checked = active;
    new bootstrap.Modal(document.getElementById('editCriterionModal')).show();
}

function openDeleteCriterionModal(id, name) {
    document.getElementById('delCritId').value = id;
    document.getElementById('delCritName').textContent = name;
    new bootstrap.Modal(document.getElementById('deleteCriterionModal')).show();
}
</script>

<jsp:include page="../common/footer.jsp" />
