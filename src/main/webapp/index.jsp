<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${not empty sessionScope.currentUser}">
        <c:redirect url="${sessionScope.currentUser.defaultDashboardUrl}"/>
    </c:when>
    <c:otherwise>
        <c:redirect url="/auth/login"/>
    </c:otherwise>
</c:choose>
