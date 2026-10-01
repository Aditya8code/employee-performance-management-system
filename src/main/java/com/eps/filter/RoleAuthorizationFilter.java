package com.eps.filter;

import com.eps.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Enforces role-based authorization:
 * - /admin/* -> Only ADMIN role
 * - /manager/* -> Only MANAGER (or ADMIN)
 * - /employee/* -> Only EMPLOYEE (or ADMIN)
 */
@WebFilter(filterName = "RoleAuthorizationFilter", urlPatterns = {"/admin/*", "/manager/*", "/employee/*"})
public class RoleAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            chain.doFilter(request, response);
            return;
        }

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());
        String role = currentUser.getRole();

        boolean authorized = true;

        if (path.startsWith("/admin/")) {
            if (!"ADMIN".equalsIgnoreCase(role)) {
                authorized = false;
            }
        } else if (path.startsWith("/manager/")) {
            if (!"MANAGER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                authorized = false;
            }
        } else if (path.startsWith("/employee/")) {
            if (!"EMPLOYEE".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                authorized = false;
            }
        }

        if (!authorized) {
            session.setAttribute("flashError", "Access Denied: You do not have permission to access that area.");
            httpResponse.sendRedirect(contextPath + currentUser.getDefaultDashboardUrl());
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
