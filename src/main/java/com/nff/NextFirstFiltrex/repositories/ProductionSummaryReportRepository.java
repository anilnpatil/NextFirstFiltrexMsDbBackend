package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;
import java.util.List;

import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;

public interface ProductionSummaryReportRepository {

    List<ProductionSummaryReportRow> fetchDailySummary(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift);

    List<ProductionSummaryReportRow> fetchWeeklySummary(
            int year,
            Integer sku,
            Integer shift);

    List<ProductionTotalsRow> fetchMonthlySummary(
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth,
            Integer sku,
            Integer shift);
}
