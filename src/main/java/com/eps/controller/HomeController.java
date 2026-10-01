package com.eps.controller;

import com.eps.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Root Controller that routes root path requests to user's dashboard or login.
 */
@WebServlet(name = "HomeController", urlPatterns = {""})
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + currentUser.getDefaultDashboardUrl());
        } else {
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }
}
