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

//     List<ProductionReportRow> getReport(
//             LocalDate from,
//             LocalDate to,
//             String sku,
//             Integer shift);

    List<ProductionReportRow> getDailySummaryReport(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift);

    List<ProductionReportRow> getWeeklySummaryReport(
            int year,
            String sku,
            Integer shift);

    List<ProductionReportRow> getMonthlySummaryReport(
            int year,
            String sku,
            Integer shift);
}
