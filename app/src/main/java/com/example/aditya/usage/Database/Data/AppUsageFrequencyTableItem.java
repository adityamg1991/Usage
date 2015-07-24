package com.example.aditya.usage.Database.Data;

/**
 * Created by aditya on 06/07/15.
 */
public class AppUsageFrequencyTableItem {

    private String label, package_name, firstUsed, lastUsed;
    double averageUseTime, totalUseTime;
    long frequency;

    public String getFirstUsed() {
        return firstUsed;
    }

    public void setFirstUsed(String firstUsed) {
        this.firstUsed = firstUsed;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
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
