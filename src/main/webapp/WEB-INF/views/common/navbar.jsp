<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-dark-subtle px-3 py-2 sticky-top">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/">
            <i class="bi bi-award-fill text-primary fs-4"></i>
            <span>EPS Enterprise</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav ms-auto align-items-center gap-3">
                <c:if test="${not empty sessionScope.currentUser}">
                    <li class="nav-item d-flex align-items-center gap-2 text-white">
                        <i class="bi bi-person-circle fs-5 text-secondary"></i>
                        <div>
                            <span class="fw-semibold">${sessionScope.currentUser.fullName}</span>
                            <span class="badge ${sessionScope.currentUser.role == 'ADMIN' ? 'bg-danger' : (sessionScope.currentUser.role == 'MANAGER' ? 'bg-primary' : 'bg-success')} ms-1">
                                ${sessionScope.currentUser.role}
                            </span>
                        </div>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light btn-sm d-flex align-items-center gap-1" href="${pageContext.request.contextPath}/auth/logout">
                            <i class="bi bi-box-arrow-right"></i> Sign Out
                        </a>
                    </li>
                </c:if>
                <c:if test="${empty sessionScope.currentUser}">
                    <li class="nav-item">
                        <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/auth/login">
                            <i class="bi bi-box-arrow-in-right"></i> Sign In
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
