package com.nff.NextFirstFiltrex.services;

import com.nff.NextFirstFiltrex.dto.RejectionTypeProjection;

import java.time.LocalDate;
import java.util.List;

public interface RejectionGraphService {

    List<RejectionTypeProjection> getDaily(
            LocalDate from, LocalDate to, Integer sku, Integer shift);

    List<RejectionTypeProjection> getWeekly(
            int year, Integer sku, Integer shift);

    List<RejectionTypeProjection> getMonthly(
            int year, Integer sku, Integer shift);
}