package com.nff.NextFirstFiltrex.services;

import java.time.LocalDate;
import java.util.List;

import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;


public interface ProductionSummaryReportService {


    List<ProductionSummaryReportRow> getDailySummaryReport(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift);

    List<ProductionSummaryReportRow> getWeeklySummaryReport(
            int year,
            Integer sku,
            Integer shift);

    List<ProductionSummaryReportRow> getMonthlySummaryReport(
            int year,
            Integer sku,
            Integer shift);
}