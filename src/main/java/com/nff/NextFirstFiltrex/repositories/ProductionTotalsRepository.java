package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;
import java.util.List;

import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;

public interface ProductionTotalsRepository {

    List<ProductionTotalsRow> fetchDailyTotals(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift);

    List<ProductionSummaryReportRow> fetchWeeklyRowsForYear(
            int year,
            Integer sku,
            Integer shift);

    List<ProductionTotalsRow> fetchMonthlyTotals(
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth,
            Integer sku,
            Integer shift);
}
