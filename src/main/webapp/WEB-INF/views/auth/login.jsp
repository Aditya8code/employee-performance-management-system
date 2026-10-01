<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="pageTitle" value="Sign In - Employee Performance Management System" />
</jsp:include>

<div class="min-vh-100 d-flex flex-column justify-content-center align-items-center bg-light py-5">
    <div class="container" style="max-width: 480px;">
        <div class="text-center mb-4">
            <div class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle mb-3 shadow" style="width: 64px; height: 64px;">
                <i class="bi bi-award-fill fs-2"></i>
            </div>
            <h2 class="fw-bold text-dark">EPS Enterprise</h2>
            <p class="text-muted">Sign in to your performance management account</p>
        </div>

        <jsp:include page="../common/alerts.jsp" />

        <div class="card shadow-sm border-0 rounded-4">
            <div class="card-body p-4 p-md-5">
                <form action="${pageContext.request.contextPath}/auth/login" method="post">
                    <input type="hidden" name="redirect" value="${param.redirect}" />

                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold text-secondary">Username or Email</label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control border-start-0 ps-0" id="username" name="username" 
                                   value="${enteredUsername != null ? enteredUsername : ''}" placeholder="e.g. admin or emp1" required autofocus />
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold text-secondary">Password</label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control border-start-0 ps-0" id="password" name="password" 
                                   placeholder="Enter your password" required />
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary btn-lg fw-semibold shadow-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Sign In
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Demo Accounts Quick-Fill Box -->
        <div class="card shadow-sm border-0 rounded-4 mt-4 bg-white">
            <div class="card-body p-3">
                <h6 class="fw-bold text-dark mb-2 text-center small text-uppercase tracking-wider">
                    <i class="bi bi-lightning-charge-fill text-warning me-1"></i> Quick Demo Access
                </h6>
                <div class="d-flex flex-wrap gap-2 justify-content-center">
                    <button type="button" class="btn btn-sm btn-outline-danger" onclick="fillCredentials('admin', 'Admin@123')">
                        <i class="bi bi-shield-lock me-1"></i> Admin
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="fillCredentials('manager1', 'Manager@123')">
                        <i class="bi bi-briefcase me-1"></i> Manager 1
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="fillCredentials('manager2', 'Manager@123')">
                        <i class="bi bi-briefcase me-1"></i> Manager 2
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-success" onclick="fillCredentials('emp1', 'Employee@123')">
                        <i class="bi bi-person me-1"></i> Employee (Alice)
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-success" onclick="fillCredentials('emp3', 'Employee@123')">
                        <i class="bi bi-person me-1"></i> Employee (Charlie)
                    </button>
                </div>
            </div>
        </div>

        <div class="text-center mt-3 text-muted small">
            Secure BCrypt Hashed Authentication &bull; Role Authorization Filter
        </div>
    </div>
</div>

<script>
function fillCredentials(user, pass) {
    document.getElementById('username').value = user;
    document.getElementById('password').value = pass;
}
</script>

<jsp:include page="../common/footer.jsp" />
