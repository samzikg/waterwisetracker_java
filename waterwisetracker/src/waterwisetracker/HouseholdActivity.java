/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 */
public class HouseholdActivity extends WaterActivity {
    private String category;
    private int duration;
    
    public HouseholdActivity(String activityId, double waterAmount, String description,
                           String category, int duration) {
        super(activityId, waterAmount, description);
        this.category = category;
        this.duration = duration;
    }
    
    @Override
    public double calculateWaterUsage() {
        return waterAmount * duration;
    }
    
    public String getCategory() {
        return category;
    }
}

