<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="sidebar p-0">
    <div class="sidebar-heading">Navigation</div>
    <ul class="sidebar-nav">
        <!-- ADMIN NAVIGATION -->
        <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
            <li>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link ${param.activeNav == 'dashboard' ? 'active' : ''}">
                    <i class="bi bi-speedometer2"></i> Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/departments" class="sidebar-link ${param.activeNav == 'departments' ? 'active' : ''}">
                    <i class="bi bi-buildings"></i> Departments
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/employees" class="sidebar-link ${param.activeNav == 'employees' ? 'active' : ''}">
                    <i class="bi bi-people"></i> Employees
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/criteria" class="sidebar-link ${param.activeNav == 'criteria' ? 'active' : ''}">
                    <i class="bi bi-check2-circle"></i> Criteria & Weights
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/cycles" class="sidebar-link ${param.activeNav == 'cycles' ? 'active' : ''}">
                    <i class="bi bi-calendar-range"></i> Evaluation Cycles
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/reports" class="sidebar-link ${param.activeNav == 'reports' ? 'active' : ''}">
                    <i class="bi bi-bar-chart-line"></i> Performance Reports
                </a>
            </li>
        </c:if>

        <!-- MANAGER NAVIGATION -->
        <c:if test="${sessionScope.currentUser.role == 'MANAGER'}">
            <li>
                <a href="${pageContext.request.contextPath}/manager/dashboard" class="sidebar-link ${param.activeNav == 'dashboard' ? 'active' : ''}">
                    <i class="bi bi-speedometer2"></i> Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/manager/team" class="sidebar-link ${param.activeNav == 'team' ? 'active' : ''}">
                    <i class="bi bi-people-fill"></i> My Team Appraisals
                </a>
            </li>
        </c:if>

        <!-- EMPLOYEE NAVIGATION -->
        <c:if test="${sessionScope.currentUser.role == 'EMPLOYEE'}">
            <li>
                <a href="${pageContext.request.contextPath}/employee/dashboard" class="sidebar-link ${param.activeNav == 'dashboard' ? 'active' : ''}">
                    <i class="bi bi-speedometer2"></i> My Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/employee/history" class="sidebar-link ${param.activeNav == 'history' ? 'active' : ''}">
                    <i class="bi bi-clock-history"></i> Appraisal History
                </a>
            </li>
        </c:if>
    </ul>
</aside>
