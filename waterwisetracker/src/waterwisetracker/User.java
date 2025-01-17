/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 * Represents a user in the WaterWise Tracker system
 */
public class User {
   private String userId;
    private String username;
    private String email;
    private String password;
    private WaterUsageTracker tracker;
    
    public User(String userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.tracker = new WaterUsageTracker();
    }
    
    public void registerUser() {
        
    }
    
    public boolean login() {
        
        return false;
    }
    
    public void updateProfile() {
       
    }
    
    public void viewWaterUsage() {
        
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public WaterUsageTracker getTracker() { return tracker; }
}
