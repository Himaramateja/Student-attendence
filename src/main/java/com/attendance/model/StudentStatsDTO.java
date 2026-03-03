package com.attendance.model;

public class StudentStatsDTO {
    private int totalConducted;
    private int totalAttended;
    private double overallPercentage;

    public StudentStatsDTO(int totalConducted, int totalAttended) {
        this.totalConducted = totalConducted;
        this.totalAttended = totalAttended;
        this.overallPercentage = totalConducted == 0 ? 0 : (double) totalAttended / totalConducted * 100;
    }

    public int getTotalConducted() {
        return totalConducted;
    }

    public int getTotalAttended() {
        return totalAttended;
    }

    public double getOverallPercentage() {
        return overallPercentage;
    }
}
