package com.eps.model;

/**
 * Admin domain entity representing system administrator.
 * Demonstrates OOP Inheritance and Polymorphism.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(Integer id, String username, String passwordHash, 
                 String fullName, String email, String phone, String status) {
        super(id, username, passwordHash, "ADMIN", fullName, email, phone, status);
    }

    @Override
    public String getRoleDisplayName() {
        return "Administrator";
    }

    @Override
    public String getDefaultDashboardUrl() {
        return "/admin/dashboard";
    }

    public boolean canManageSystem() {
        return true;
    }
}
