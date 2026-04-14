package com.nff.NextFirstFiltrex.services;

import java.time.LocalDate;
import java.util.List;

import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;

public interface ProductionTotalsService {

    List<ProductionTotalsRow> getDailyTotals(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift);

    List<ProductionTotalsRow> getWeeklyTotals(
            int year,
            Integer sku,
            Integer shift);

    List<ProductionTotalsRow> getMonthlyTotals(
            int year,
            Integer sku,
            Integer shift);
}