/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *Interface defining database operations for the WaterWise Tracker system
 * @author SAM ZI KANG
 */

public interface DatabaseManager {
    void connect();
    void disconnect();
    void saveData();
    void loadData();
    void updateData();
}