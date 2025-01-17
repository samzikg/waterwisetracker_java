/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *Implementation of DatabaseManager for SQLite database operations
 * @author SAM ZI KANG
 */
import java.util.Properties;
import java.sql.*;

public class SQLiteManager implements DatabaseManager {
    private Connection conn;
    private static final String DB_URL = "jdbc:sqlite:waterwise.db";
    
    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            // Add connection properties to handle concurrent access
            Properties props = new Properties();
            props.setProperty("busy_timeout", "30000"); // 30 second timeout
            props.setProperty("journal_mode", "WAL"); // Write-Ahead Logging mode
            conn = DriverManager.getConnection(DB_URL, props);
            conn.setAutoCommit(false); // Enable transaction control
            initializeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   @Override
    public void disconnect() {
        try {
            if (conn != null) {
                conn.commit(); // Commit any pending transactions
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeTables() {
    try (Statement stmt = conn.createStatement()) {
        // Create Users table
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "userId TEXT PRIMARY KEY," +
                "username TEXT NOT NULL UNIQUE," + // Ensure uniqueness of username
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL)");
        System.out.println("Users table created or already exists.");
        
        // Create WaterActivity table
        stmt.execute("CREATE TABLE IF NOT EXISTS water_activities (" +
                "activityId TEXT PRIMARY KEY," +
                "userId TEXT," +
                "waterAmount REAL," +
                "timestamp TEXT," +
                "description TEXT," +
                "category TEXT," +
                "FOREIGN KEY(userId) REFERENCES users(userId))");
        System.out.println("Water activities table created or already exists.");
    } catch (SQLException e) {
        System.err.println("Error creating tables: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    @Override
    public void saveData() {
        // Implementation for saving data
    }
    
    @Override
    public void loadData() {
        // Implementation for loading data
    }
    
    @Override
    public void updateData() {
        // Implementation for updating data
    }
    public User verifyLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean registerUser(String username, String email, String password) {
    try {
        // Check if username already exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, username);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Username already exists.");
            return false;
        }

        // Insert new user
        String sql = "INSERT INTO users (userId, username, email, password) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        String userId = generateUserId();
        pstmt.setString(1, userId);
        pstmt.setString(2, username);
        pstmt.setString(3, email);
        pstmt.setString(4, password);

        int result = pstmt.executeUpdate();
        conn.commit();
        return result > 0;
    } catch (SQLException e) {
        System.err.println("Error during registration: " + e.getMessage());
        e.printStackTrace();
        try {
            conn.rollback();
        } catch (SQLException rollbackEx) {
            rollbackEx.printStackTrace();
        }
        return false;
    }
}

private String generateUserId() {
    // Simple implementation - you might want to use UUID or another method
    return "U" + System.currentTimeMillis();
}

public synchronized boolean saveWaterActivity(WaterActivity activity, String userId) {
        try {
            String sql = "INSERT INTO water_activities (activityId, userId, waterAmount, timestamp, description, category) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, activity.getActivityId());
            pstmt.setString(2, userId);
            pstmt.setDouble(3, activity.getWaterAmount());
            pstmt.setString(4, activity.getTimestamp().toString());
            pstmt.setString(5, activity.getDescription());
            
            String category = activity instanceof HouseholdActivity ? "Household" : "Irrigation";
            pstmt.setString(6, category);
            
            int result = pstmt.executeUpdate();
            conn.commit(); // Commit the transaction
            return result > 0;
        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatePassword(String userId, String currentPassword, String newPassword) {
    try {
        // First verify the current password
        String verifySql = "SELECT COUNT(*) FROM users WHERE userId = ? AND password = ?";
        PreparedStatement verifyStmt = conn.prepareStatement(verifySql);
        verifyStmt.setString(1, userId);
        verifyStmt.setString(2, currentPassword);
        
        ResultSet rs = verifyStmt.executeQuery();
        if (rs.next() && rs.getInt(1) == 0) {
            return false; // Current password is incorrect
        }
        
        // Update the password
        String updateSql = "UPDATE users SET password = ? WHERE userId = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setString(1, newPassword);
        updateStmt.setString(2, userId);
        
        int result = updateStmt.executeUpdate();
        conn.commit();
        return result > 0;
    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
        return false;
    }
}
 }
