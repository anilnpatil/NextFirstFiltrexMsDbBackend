package com.nff.NextFirstFiltrex.dto;

public class ProductionSummary {
    private long okParts;
    private long notOkParts;
    private long totalProduction;
    private Integer shift;

    public ProductionSummary(long okParts, long notOkParts, long totalProduction, Integer shift) {
        this.okParts = okParts;
        this.notOkParts = notOkParts;
        this.totalProduction = totalProduction;
        this.shift = shift;
    }

    // Getters and setters
    public long getOkParts() {
        return okParts;
    }

    public void setOkParts(long okParts) {
        this.okParts = okParts;
    }

    public long getNotOkParts() {
        return notOkParts;
    }

    public void setNotOkParts(long notOkParts) {
        this.notOkParts = notOkParts;
    }

    public long getTotalProduction() {
        return totalProduction;
    }

    public void setTotalProduction(long totalProduction) {
        this.totalProduction = totalProduction;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }
}