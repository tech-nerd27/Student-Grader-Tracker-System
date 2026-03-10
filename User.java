// User.java
// Purpose: Represents a user for the login system, now including a role.
// Demonstrates: Encapsulation.

import java.io.Serializable;

public class User implements Serializable {
    // Encapsulated fields
    private String username;
    private String password; // In a real app, hash the password!
    private String role;     // Added role field (e.g., "ADMIN", "TEACHER", "STUDENT")

    // Constructor - Updated to include role
    public User(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null.");
        }
        if (role == null || role.trim().isEmpty()) {
             throw new IllegalArgumentException("Role cannot be null or empty.");
        }
        this.username = username;
        this.password = password;
        this.role = role.toUpperCase(); // Store role in uppercase for consistency
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Method to check password (no direct getter for password)
    public boolean checkPassword(String passwordAttempt) {
        return this.password.equals(passwordAttempt);
    }

    // Helper methods to check roles
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    public boolean isTeacher() {
        return "TEACHER".equals(this.role);
    }

    public boolean isStudent() {
        return "STUDENT".equals(this.role);
    }


    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}
