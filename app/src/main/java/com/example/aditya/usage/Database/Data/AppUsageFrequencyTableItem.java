package com.example.aditya.usage.Database.Data;

/**
 * Created by aditya on 06/07/15.
 */
public class AppUsageFrequencyTableItem {

    private String label, package_name;
    private double averageUseTime, totalUseTime;
    private long frequency;
    private long lastUsed;
    private long firstUsed;

    public long getFirstUsed() {
        return firstUsed;
    }

    public void setFirstUsed(long firstUsed) {
        this.firstUsed = firstUsed;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public double getAverageUseTime() {
        return averageUseTime;
    }

    public void setAverageUseTime(double averageUseTime) {
        this.averageUseTime = averageUseTime;
    }

    public double getTotalUseTime() {
        return totalUseTime;
    }

    public void setTotalUseTime(double totalUseTime) {
        this.totalUseTime = totalUseTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String package_name) {
        this.package_name = package_name;
    }
}
