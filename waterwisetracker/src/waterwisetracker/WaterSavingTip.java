/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package waterwisetracker;

/**
 *
 * @author SAM ZI KANG
 */
public class WaterSavingTip {
    private String tipId;
    private String category;
    private String description;
    private int potentialSaving; // Liters per day/week/month
    
    public WaterSavingTip(String tipId, String category, String description, int potentialSaving) {
        this.tipId = tipId;
        this.category = category;
        this.description = description;
        this.potentialSaving = potentialSaving;
    }
    
    /**
     * Display the tip information
     */
    public void displayTip() {
        System.out.println("Category: " + category);
        System.out.println("Tip: " + description);
        System.out.println("Potential Water Saving: " + potentialSaving + " L");
    }
    
    /**
     * Calculate the potential impact of implementing this tip
     * @return The amount of water that could be saved
     */
    public int calculatePotentialImpact() {
        return potentialSaving;
    }
    
    // Getters
    public String getTipId() { return tipId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getPotentialSaving() { return potentialSaving; }
}

