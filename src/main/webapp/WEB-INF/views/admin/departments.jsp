<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Departments Management - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="departments" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Departments Management</h3>
                <p class="text-muted mb-0">Organize business units and departmental allocations.</p>
            </div>
            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addDeptModal">
                <i class="bi bi-plus-lg me-1"></i> Add Department
            </button>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card">
            <div class="card-header fw-bold">
                <i class="bi bi-buildings me-2"></i>Department Directory (${departments.size()})
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>#ID</th>
                            <th>Department Name</th>
                            <th>Description</th>
                            <th>Total Employees</th>
                            <th>Created Date</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dept" items="${departments}">
                            <tr>
                                <td>${dept.id}</td>
                                <td class="fw-semibold text-primary">${dept.name}</td>
                                <td class="text-muted text-truncate" style="max-width: 320px;">
                                    ${dept.description != null ? dept.description : "-"}
                                </td>
                                <td>
                                    <span class="badge bg-secondary-subtle text-secondary px-3 py-2 rounded-pill">
                                        <i class="bi bi-person me-1"></i>${dept.employeeCount} staff
                                    </span>
                                </td>
                                <td>${dept.createdAt != null ? dept.createdAt : "-"}</td>
                                <td class="text-end">
                                    <button class="btn btn-sm btn-outline-secondary me-1" 
                                            onclick="openEditDeptModal(${dept.id}, '${dept.name}', '${dept.description}')">
                                        <i class="bi bi-pencil"></i> Edit
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger" 
                                            onclick="openDeleteDeptModal(${dept.id}, '${dept.name}', ${dept.employeeCount})">
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

<!-- Modal: Add Department -->
<div class="modal fade" id="addDeptModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/departments" method="post" class="modal-content">
            <input type="hidden" name="action" value="create" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Add New Department</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Department Name</label>
                    <input type="text" name="name" class="form-control" placeholder="e.g. Finance & Accounting" required />
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Description</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Brief summary of department responsibilities..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Department</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit Department -->
<div class="modal fade" id="editDeptModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/departments" method="post" class="modal-content">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" id="editDeptId" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Edit Department</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Department Name</label>
                    <input type="text" name="name" id="editDeptName" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Description</label>
                    <textarea name="description" id="editDeptDesc" class="form-control" rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Department</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Delete Department -->
<div class="modal fade" id="deleteDeptModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/departments" method="post" class="modal-content">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="id" id="delDeptId" />
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title fw-bold">Confirm Deletion</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete the department <strong id="delDeptName"></strong>?</p>
                <div id="delDeptWarning" class="alert alert-warning small d-none">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    This department currently has employees assigned and cannot be deleted. Reassign employees first.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" id="delDeptSubmitBtn" class="btn btn-danger">Delete</button>
            </div>
        </form>
    </div>
</div>

<script>
function openEditDeptModal(id, name, desc) {
    document.getElementById('editDeptId').value = id;
    document.getElementById('editDeptName').value = name;
    document.getElementById('editDeptDesc').value = desc || '';
    new bootstrap.Modal(document.getElementById('editDeptModal')).show();
}

function openDeleteDeptModal(id, name, empCount) {
    document.getElementById('delDeptId').value = id;
    document.getElementById('delDeptName').textContent = name;
    const warning = document.getElementById('delDeptWarning');
    const submitBtn = document.getElementById('delDeptSubmitBtn');
    if (empCount > 0) {
        warning.classList.remove('d-none');
        submitBtn.disabled = true;
    } else {
        warning.classList.add('d-none');
        submitBtn.disabled = false;
    }
    new bootstrap.Modal(document.getElementById('deleteDeptModal')).show();
}
</script>

<jsp:include page="../common/footer.jsp" />
