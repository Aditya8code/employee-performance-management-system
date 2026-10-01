<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Evaluation Cycles - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="cycles" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Evaluation Cycles</h3>
                <p class="text-muted mb-0">Create review windows and control active appraisal submission periods.</p>
            </div>
            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addCycleModal">
                <i class="bi bi-calendar-plus me-1"></i> New Evaluation Cycle
            </button>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-calendar-range me-2"></i>Appraisal Review Cycles (${cycles.size()})
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Cycle Name</th>
                            <th>Date Window</th>
                            <th>Status</th>
                            <th>Evaluations Progress</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cyc" items="${cycles}">
                            <tr>
                                <td>
                                    <div class="fw-bold text-dark">${cyc.name}</div>
                                    <div class="small text-muted text-truncate" style="max-width: 300px;">
                                        ${cyc.description != null ? cyc.description : "-"}
                                    </div>
                                </td>
                                <td>
                                    <div><i class="bi bi-calendar-event text-primary me-1"></i>${cyc.startDate} to ${cyc.endDate}</div>
                                </td>
                                <td>
                                    <span class="badge ${cyc.statusBadgeClass} fs-6 px-3 py-1 rounded-pill">
                                        ${cyc.status}
                                    </span>
                                </td>
                                <td>
                                    <div>
                                        <span class="fw-semibold text-success">${cyc.completedEvaluations} completed</span>
                                        <span class="text-muted">/ ${cyc.totalEvaluations} total</span>
                                    </div>
                                </td>
                                <td class="text-end">
                                    <c:if test="${cyc.status == 'DRAFT'}">
                                        <form action="${pageContext.request.contextPath}/admin/cycles" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="status" />
                                            <input type="hidden" name="id" value="${cyc.id}" />
                                            <input type="hidden" name="status" value="ACTIVE" />
                                            <button type="submit" class="btn btn-sm btn-outline-success me-1" title="Activate Cycle">
                                                <i class="bi bi-play-circle me-1"></i> Activate
                                            </button>
                                        </form>
                                    </c:if>
                                    <c:if test="${cyc.status == 'ACTIVE'}">
                                        <form action="${pageContext.request.contextPath}/admin/cycles" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="status" />
                                            <input type="hidden" name="id" value="${cyc.id}" />
                                            <input type="hidden" name="status" value="COMPLETED" />
                                            <button type="submit" class="btn btn-sm btn-outline-secondary me-1" title="Complete Cycle">
                                                <i class="bi bi-check2-circle me-1"></i> Close / Complete
                                            </button>
                                        </form>
                                    </c:if>

                                    <button class="btn btn-sm btn-outline-primary me-1"
                                            onclick="openEditCycleModal(${cyc.id}, '${cyc.name}', '${cyc.description}', '${cyc.startDate}', '${cyc.endDate}', '${cyc.status}')">
                                        <i class="bi bi-pencil"></i> Edit
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="openDeleteCycleModal(${cyc.id}, '${cyc.name}')">
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

<!-- Modal: Add Cycle -->
<div class="modal fade" id="addCycleModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/cycles" method="post" class="modal-content">
            <input type="hidden" name="action" value="create" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Create Evaluation Cycle</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Cycle Name</label>
                    <input type="text" name="name" class="form-control" placeholder="e.g. Q4 2026 Performance Appraisal" required />
                </div>
                <div class="row g-2 mb-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Start Date</label>
                        <input type="date" name="startDate" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">End Date</label>
                        <input type="date" name="endDate" class="form-control" required />
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Cycle Description / Scope</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Context and goals for this appraisal period..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Create Cycle</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit Cycle -->
<div class="modal fade" id="editCycleModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/cycles" method="post" class="modal-content">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" id="editCycId" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Edit Evaluation Cycle</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Cycle Name</label>
                    <input type="text" name="name" id="editCycName" class="form-control" required />
                </div>
                <div class="row g-2 mb-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Start Date</label>
                        <input type="date" name="startDate" id="editCycStartDate" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">End Date</label>
                        <input type="date" name="endDate" id="editCycEndDate" class="form-control" required />
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Status</label>
                    <select name="status" id="editCycStatus" class="form-select">
                        <option value="DRAFT">DRAFT</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="COMPLETED">COMPLETED</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Description</label>
                    <textarea name="description" id="editCycDesc" class="form-control" rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Cycle</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Delete Cycle -->
<div class="modal fade" id="deleteCycleModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/cycles" method="post" class="modal-content">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="id" id="delCycId" />
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title fw-bold">Confirm Deletion</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete cycle <strong id="delCycName"></strong>?</p>
                <div class="alert alert-warning small">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    All appraisals submitted during this cycle will also be deleted.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-danger">Delete Cycle</button>
            </div>
        </form>
    </div>
</div>

<script>
function openEditCycleModal(id, name, desc, start, end, status) {
    document.getElementById('editCycId').value = id;
    document.getElementById('editCycName').value = name;
    document.getElementById('editCycDesc').value = desc || '';
    document.getElementById('editCycStartDate').value = start;
    document.getElementById('editCycEndDate').value = end;
    document.getElementById('editCycStatus').value = status;
    new bootstrap.Modal(document.getElementById('editCycleModal')).show();
}

function openDeleteCycleModal(id, name) {
    document.getElementById('delCycId').value = id;
    document.getElementById('delCycName').textContent = name;
    new bootstrap.Modal(document.getElementById('deleteCycleModal')).show();
}
</script>

<jsp:include page="../common/footer.jsp" />
