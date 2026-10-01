package com.eps.controller;

import com.eps.exception.AuthenticationException;
import com.eps.model.Employee;
import com.eps.model.User;
import com.eps.service.AuthService;
import com.eps.service.EmployeeService;
import com.eps.service.impl.AuthServiceImpl;
import com.eps.service.impl.EmployeeServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling user authentication (login, logout, and credential flows).
 */
@WebServlet(name = "AuthController", urlPatterns = {"/auth/login", "/auth/logout"})
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/auth/logout".equals(path)) {
            handleLogout(request, response);
            return;
        }

        // /auth/login
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            response.sendRedirect(request.getContextPath() + user.getDefaultDashboardUrl());
            return;
        }

        String logoutParam = request.getParameter("logout");
        if ("true".equalsIgnoreCase(logoutParam)) {
            request.setAttribute("flashSuccess", "You have been logged out successfully.");
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/auth/logout".equals(path)) {
            handleLogout(request, response);
            return;
        }

        // Handle Login POST
        String identifier = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        try {
            User user = authService.authenticate(identifier, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);

            // If user is employee, cache employee domain object in session for quick view access
            if ("EMPLOYEE".equalsIgnoreCase(user.getRole())) {
                try {
                    Employee emp = employeeService.getEmployeeByUserId(user.getId());
                    session.setAttribute("currentEmployee", emp);
                } catch (Exception ignored) {
                }
            }

            String ctx = request.getContextPath();
            if (redirect != null && !redirect.trim().isEmpty() && redirect.startsWith("/") && (ctx.isEmpty() || redirect.startsWith(ctx))) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect(ctx + user.getDefaultDashboardUrl());
            }

        } catch (AuthenticationException ae) {
            request.setAttribute("errorMessage", ae.getMessage());
            request.setAttribute("enteredUsername", identifier);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred during sign in: " + ex.getMessage());
            request.setAttribute("enteredUsername", identifier);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login?logout=true");
    }
}
