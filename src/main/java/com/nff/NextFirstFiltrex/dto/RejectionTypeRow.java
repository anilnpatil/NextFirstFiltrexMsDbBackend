package com.nff.NextFirstFiltrex.dto;

public record RejectionTypeRow(String rejectionType, Long count, String period)
        implements RejectionTypeProjection {

    @Override
    public String getRejectionType() {
        return rejectionType;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public String getPeriod() {
        return period;
    }
}