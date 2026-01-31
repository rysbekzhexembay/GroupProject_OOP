package logic;

import db.DB;
import model.User;
import java.sql.*;

public class Auth {

    public User register(String name, String pass) {
        if (name == null || name.trim().isEmpty() || pass == null || pass.length() < 4) {
            System.out.println("Validation Error: Invalid username or password (min 4 chars).");
            return null;
        }
        try {
            Connection conn = DB.connect();
            String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'customer') RETURNING id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, pass);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), name, pass, "customer");
            }
        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
        return null;
    }

    public User login(String name, String pass) {
        try {
            Connection conn = DB.connect();
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                if (role == null) role = "customer"; 
                return new User(
                    rs.getInt("id"), 
                    rs.getString("username"), 
                    rs.getString("password_hash"),
                    role
                );
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }
}