/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 */
public class IrrigationActivity extends WaterActivity {
    private double areaSize;
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
        return isRainyDay ? 0 : waterAmount * areaSize;
    }
    
    public void adjustForWeather() {
        if (isRainyDay) {
            waterAmount = 0;
        }
    }
}
