/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;
import java.util.ArrayList;
import java.util.List;
/**
 * Tracks water usage activities and calculates statistics
 * @author SAM ZI KANG
 */
public class WaterUsageTracker {
    private double totalUsage;
    private List<WaterActivity> activities;
    private List<WaterSavingTip> savingTips;
    
    public WaterUsageTracker() {
        this.totalUsage = 0.0;
        this.activities = new ArrayList<>();
        this.savingTips = new ArrayList<>();
        initializeSavingTips();
    }
    
    /**
     * Add a new water activity
     * @param activity The activity to add
     */
    public void addActivity(WaterActivity activity) {
        activities.add(activity);
        totalUsage += activity.calculateWaterUsage();
    }
    
    /**
     * Calculate total water usage
     * @return Total water usage in liters
     */
    public double calculateTotalUsage() {
        totalUsage = activities.stream()
                .mapToDouble(WaterActivity::calculateWaterUsage)
                .sum();
        return totalUsage;
    }
    
    /**
     * Generate usage report
     * @return Report as string
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Water Usage Report\n");
        report.append("=================\n");
        report.append(String.format("Total Usage: %.2f L\n", totalUsage));
        report.append("\nDetailed Activities:\n");
        
        activities.forEach(activity -> {
            report.append(activity.getActivityDetails()).append("\n");
        });
        
        return report.toString();
    }
    
    /**
     * Initialize default water saving tips
     */
    private void initializeSavingTips() {
        savingTips.add(new WaterSavingTip("T1", "Household", 
            "Fix leaking faucets immediately", 20));
        savingTips.add(new WaterSavingTip("T2", "Household", 
            "Install water-efficient shower heads", 50));
        savingTips.add(new WaterSavingTip("T3", "Irrigation", 
            "Water plants early morning or evening", 30));
        savingTips.add(new WaterSavingTip("T4", "Household", 
            "Use dishwasher only when full", 40));
    }
    
    /**
     * Get water saving tips
     * @return List of water saving tips
     */
    public List<WaterSavingTip> getWaterSavingTips() {
        return savingTips;
    }
    
    /**
     * Get all water activities
     * @return List of water activities
     */
    public List<WaterActivity> getActivities() {
        return activities;
    }
    
    /**
     * Get total water usage
     * @return Total water usage in liters
     */
    public double getTotalUsage() {
        return totalUsage;
    }
}
