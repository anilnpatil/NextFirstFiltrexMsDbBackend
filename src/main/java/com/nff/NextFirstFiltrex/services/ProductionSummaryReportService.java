// package com.nff.NextFirstFiltrex.services;

// import java.time.LocalDate;
// import java.util.List;

// import com.nff.NextFirstFiltrex.dto.ProductionReportRow;

// public interface ProductionReportService {

//   public List<ProductionReportRow> getReport(
//             LocalDate from,
//             LocalDate to,
//             String sku,
//             Integer shift);

// }
package com.nff.NextFirstFiltrex.services;

import java.time.LocalDate;
import java.util.List;

import com.nff.NextFirstFiltrex.dto.ProductionReportRow;

public interface ProductionSummaryReportService {

    List<ProductionReportRow> getReport(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift);

    List<ProductionReportRow> getDailyReport(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift);

    List<ProductionReportRow> getWeeklyReport(
            int year,
            String sku,
            Integer shift);

    List<ProductionReportRow> getMonthlyReport(
            int year,
            String sku,
            Integer shift);
}
