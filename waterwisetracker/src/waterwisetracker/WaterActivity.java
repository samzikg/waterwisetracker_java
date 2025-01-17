/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 */
import java.time.LocalDateTime;

/**
 * Abstract class representing a water usage activity
 */
public abstract class WaterActivity {
    protected String activityId;
    protected double waterAmount;
    protected LocalDateTime timestamp;
    protected String description;
    
    public WaterActivity(String activityId, double waterAmount, String description) {
        this.activityId = activityId;
        this.waterAmount = waterAmount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Calculate the water usage for this activity
     * @return Amount of water used in liters
     */
    public abstract double calculateWaterUsage();
    
    /**
     * Get details of the activity
     * @return String containing activity details
     */
    public String getActivityDetails() {
        return String.format("Activity ID: %s\nWater Amount: %.2f L\nTime: %s\nDescription: %s",
                           activityId, waterAmount, timestamp, description);
    }
    
    // Getters
    public String getActivityId() { return activityId; }
    public double getWaterAmount() { return waterAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}

/**
 * Represents household water usage activities
 * 
 */


/**
 * Represents irrigation water usage activities
 */
class IrrigationActivity extends WaterActivity {
    private double areaSize; // in square meters
    private String plantType;
    private boolean isRainyDay;
    
    public IrrigationActivity(String activityId, double waterAmount, String description,
                            double areaSize, String plantType, boolean isRainyDay) {
        super(activityId, waterAmount, description);
        this.areaSize = areaSize;
        this.plantType = plantType;
        this.isRainyDay = isRainyDay;
    }
    
    @Override
    public double calculateWaterUsage() {
        if (isRainyDay) {
            return 0; // No water usage on rainy days
        }
        // Calculate based on area and plant type
        return waterAmount * areaSize;
    }
    
    public void adjustForWeather() {
        if (isRainyDay) {
            waterAmount = 0;
        }
    }
    
    // Getters
    public double getAreaSize() { return areaSize; }
    public String getPlantType() { return plantType; }
    public boolean isRainyDay() { return isRainyDay; }
}