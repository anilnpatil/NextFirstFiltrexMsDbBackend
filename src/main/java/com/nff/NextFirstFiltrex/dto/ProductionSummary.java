package com.nff.NextFirstFiltrex.dto;

import java.time.LocalDateTime;

public class ProductionSummary {
    private long okParts;
    private long notOkParts;
    private long totalProduction;
    private Integer shift;
    private String sku;
    private LocalDateTime productionDateTime;

    public ProductionSummary(long okParts, long notOkParts, long totalProduction, Integer shift, String sku, LocalDateTime productionDateTime) {
        this.okParts = okParts;
        this.notOkParts = notOkParts;
        this.totalProduction = totalProduction;
        this.shift = shift;
        this.sku = sku;
        this.productionDateTime = productionDateTime;
    }
    
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public LocalDateTime getProductionDateTime() {
        return productionDateTime;
    }

    public void setProductionDateTime(LocalDateTime productionDateTime) {
        this.productionDateTime = productionDateTime;
    }
}