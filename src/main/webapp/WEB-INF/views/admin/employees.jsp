<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Employees Directory - EPS Enterprise" />
</jsp:include>
<jsp:include page="../common/navbar.jsp" />

<div class="app-wrapper">
    <jsp:include page="../common/sidebar.jsp">
        <jsp:param name="activeNav" value="employees" />
    </jsp:include>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Employee Directory</h3>
                <p class="text-muted mb-0">Manage staff profiles, department assignments, and supervisor reporting lines.</p>
            </div>
            <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addEmpModal">
                <i class="bi bi-person-plus-fill me-1"></i> Register Employee
            </button>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <span class="fw-bold"><i class="bi bi-people me-2"></i>All Employees (${employees.size()})</span>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Employee</th>
                            <th>Job Title</th>
                            <th>Department</th>
                            <th>Assigned Manager</th>
                            <th>Hire Date</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="emp" items="${employees}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center gap-2">
                                        <div class="rounded-circle bg-primary-subtle text-primary fw-bold d-flex align-items-center justify-content-center" style="width: 38px; height: 38px;">
                                            ${emp.fullName.substring(0, 1)}
                                        </div>
                                        <div>
                                            <div class="fw-semibold text-dark">${emp.fullName}</div>
                                            <div class="small text-muted">${emp.email} &bull; @${emp.username}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>${emp.jobTitle}</td>
                                <td>
                                    <span class="badge bg-light text-dark border">
                                        ${emp.departmentName}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty emp.managerName}">
                                            <span class="text-dark fw-medium"><i class="bi bi-person-badge text-primary me-1"></i>${emp.managerName}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">Unassigned</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${emp.hireDate}</td>
                                <td>
                                    <span class="badge ${emp.active ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
                                        ${emp.status}
                                    </span>
                                </td>
                                <td class="text-end">
                                    <button class="btn btn-sm btn-outline-primary me-1" title="Assign Manager"
                                            onclick="openAssignManagerModal(${emp.employeeId}, '${emp.fullName}', ${emp.managerId != null ? emp.managerId : -1})">
                                        <i class="bi bi-person-gear"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-secondary me-1" title="Edit"
                                            onclick="openEditEmpModal(${emp.employeeId}, '${emp.fullName}', '${emp.email}', '${emp.phone}', ${emp.departmentId}, ${emp.managerId != null ? emp.managerId : -1}, '${emp.jobTitle}', '${emp.hireDate}', '${emp.salary}', '${emp.status}')">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger" title="Delete"
                                            onclick="openDeleteEmpModal(${emp.employeeId}, '${emp.fullName}')">
                                        <i class="bi bi-trash"></i>
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

<!-- Modal: Add Employee -->
<div class="modal fade" id="addEmpModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <form action="${pageContext.request.contextPath}/admin/employees" method="post" class="modal-content">
            <input type="hidden" name="action" value="create" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Register New Employee</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Username <span class="text-danger">*</span></label>
                        <input type="text" name="username" class="form-control" placeholder="e.g. jdoe" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Initial Password <span class="text-danger">*</span></label>
                        <input type="password" name="password" class="form-control" placeholder="Min 6 characters" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Full Name <span class="text-danger">*</span></label>
                        <input type="text" name="fullName" class="form-control" placeholder="e.g. John Doe" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Email Address <span class="text-danger">*</span></label>
                        <input type="email" name="email" class="form-control" placeholder="john.doe@company.com" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Phone Number</label>
                        <input type="text" name="phone" class="form-control" placeholder="+1-555-0123" />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Department <span class="text-danger">*</span></label>
                        <select name="departmentId" class="form-select" required>
                            <option value="">-- Select Department --</option>
                            <c:forEach var="d" items="${departments}">
                                <option value="${d.id}">${d.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Assign Manager (Reporting Lead)</label>
                        <select name="managerId" class="form-select">
                            <option value="-1">-- None (Unassigned) --</option>
                            <c:forEach var="m" items="${managers}">
                                <option value="${m.id}">${m.fullName} (${m.email})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Job Title <span class="text-danger">*</span></label>
                        <input type="text" name="jobTitle" class="form-control" placeholder="e.g. Software Engineer" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Hire Date <span class="text-danger">*</span></label>
                        <input type="date" name="hireDate" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Annual Salary ($)</label>
                        <input type="number" step="0.01" name="salary" class="form-control" placeholder="85000.00" />
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Create Employee Profile</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit Employee -->
<div class="modal fade" id="editEmpModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <form action="${pageContext.request.contextPath}/admin/employees" method="post" class="modal-content">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="employeeId" id="editEmpId" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Edit Employee Details</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Full Name</label>
                        <input type="text" name="fullName" id="editEmpFullName" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Email Address</label>
                        <input type="email" name="email" id="editEmpEmail" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Phone Number</label>
                        <input type="text" name="phone" id="editEmpPhone" class="form-control" />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Department</label>
                        <select name="departmentId" id="editEmpDeptId" class="form-select" required>
                            <c:forEach var="d" items="${departments}">
                                <option value="${d.id}">${d.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Assigned Manager</label>
                        <select name="managerId" id="editEmpMgrId" class="form-select">
                            <option value="-1">-- None (Unassigned) --</option>
                            <c:forEach var="m" items="${managers}">
                                <option value="${m.id}">${m.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Job Title</label>
                        <input type="text" name="jobTitle" id="editEmpJobTitle" class="form-control" required />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Hire Date</label>
                        <input type="date" name="hireDate" id="editEmpHireDate" class="form-control" required />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Annual Salary ($)</label>
                        <input type="number" step="0.01" name="salary" id="editEmpSalary" class="form-control" />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Account Status</label>
                        <select name="status" id="editEmpStatus" class="form-select">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="INACTIVE">INACTIVE</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Profile</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Assign Manager -->
<div class="modal fade" id="assignManagerModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/employees" method="post" class="modal-content">
            <input type="hidden" name="action" value="assign_manager" />
            <input type="hidden" name="employeeId" id="assignEmpId" />
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Assign Manager / Supervisor</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Select the manager responsible for reviewing <strong id="assignEmpName"></strong>:</p>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Manager</label>
                    <select name="managerId" id="assignMgrSelect" class="form-select">
                        <option value="-1">-- Unassigned --</option>
                        <c:forEach var="m" items="${managers}">
                            <option value="${m.id}">${m.fullName} (${m.email})</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Reporting Assignment</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Delete Employee -->
<div class="modal fade" id="deleteEmpModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/admin/employees" method="post" class="modal-content">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="employeeId" id="delEmpId" />
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title fw-bold">Confirm Employee Deletion</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to permanently delete employee <strong id="delEmpName"></strong> and their associated user account?</p>
                <div class="alert alert-warning small">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    All performance evaluation history linked to this employee will also be deleted.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-danger">Delete Employee</button>
            </div>
        </form>
    </div>
</div>

<script>
function openAssignManagerModal(empId, empName, currentMgrId) {
    document.getElementById('assignEmpId').value = empId;
    document.getElementById('assignEmpName').textContent = empName;
    document.getElementById('assignMgrSelect').value = currentMgrId;
    new bootstrap.Modal(document.getElementById('assignManagerModal')).show();
}

function openEditEmpModal(empId, fullName, email, phone, deptId, mgrId, jobTitle, hireDate, salary, status) {
    document.getElementById('editEmpId').value = empId;
    document.getElementById('editEmpFullName').value = fullName;
    document.getElementById('editEmpEmail').value = email;
    document.getElementById('editEmpPhone').value = phone || '';
    document.getElementById('editEmpDeptId').value = deptId;
    document.getElementById('editEmpMgrId').value = mgrId;
    document.getElementById('editEmpJobTitle').value = jobTitle;
    document.getElementById('editEmpHireDate').value = hireDate;
    document.getElementById('editEmpSalary').value = salary || '0.00';
    document.getElementById('editEmpStatus').value = status;
    new bootstrap.Modal(document.getElementById('editEmpModal')).show();
}

function openDeleteEmpModal(empId, empName) {
    document.getElementById('delEmpId').value = empId;
    document.getElementById('delEmpName').textContent = empName;
    new bootstrap.Modal(document.getElementById('deleteEmpModal')).show();
}
</script>

<jsp:include page="../common/footer.jsp" />
