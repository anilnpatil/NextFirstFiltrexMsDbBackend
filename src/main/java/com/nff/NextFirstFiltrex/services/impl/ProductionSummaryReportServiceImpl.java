// package com.nff.NextFirstFiltrex.services.impl;

// import java.time.LocalDate;
// import java.time.YearMonth;
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;
// import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
// import com.nff.NextFirstFiltrex.repositories.DailyAggReportRepository;
// import com.nff.NextFirstFiltrex.repositories.MonthlyAggReportRepository;
// import com.nff.NextFirstFiltrex.repositories.RawReportRepository;
// import com.nff.NextFirstFiltrex.services.ProductionReportService;

// @Service
// @RequiredArgsConstructor
// public class ProductionReportServiceImpl implements ProductionReportService {

//     private final RawReportRepository rawRepo;
//     private final DailyAggReportRepository dailyRepo;
//     private final MonthlyAggReportRepository monthlyRepo;
    
//     @Override
//     public List<ProductionReportRow> getReport(
//             LocalDate from,
//             LocalDate to,
//             String sku,
//             Integer shift) {

//         if (from.isAfter(to)) {
//             throw new IllegalArgumentException("From date must be <= To date");
//         }

//         LocalDate today = LocalDate.now();
//         LocalDate yesterday = today.minusDays(1);

//         List<ProductionReportRow> result = new ArrayList<>();

//         /*1️⃣ RAW → TODAY * */
//         if (!from.isAfter(today) && !to.isBefore(today)) {
//             result.addAll(rawRepo.todayReport(today, sku, shift));
//         }

//         /* 2️⃣ DAILY → FROM → YESTERDAY * */
//         LocalDate dailyFrom = from;
//         LocalDate dailyTo = min(to, yesterday);

//         if (!dailyFrom.isAfter(dailyTo)) {
//             result.addAll(dailyRepo.range(dailyFrom, dailyTo, sku, shift));
//         }

//         /*  3️⃣ MONTHLY → FULLY CLOSED MONTHS * */
//         YearMonth reqStart = YearMonth.from(from);
//         YearMonth reqEnd = YearMonth.from(to);
//         YearMonth lastFullMonth = YearMonth.from(today.minusMonths(1));

//         if (reqStart.isBefore(lastFullMonth)) {
//             YearMonth monthEnd = minYm(reqEnd, lastFullMonth);

//             result.addAll(
//                     monthlyRepo.range(
//                             reqStart.getYear(), reqStart.getMonthValue(),
//                             monthEnd.getYear(), monthEnd.getMonthValue(),
//                             sku, shift
//                     )
//             );
//         }

//         /*4️⃣ SORT → NEWEST FIRST  **/
//         result.sort(
//     Comparator
//         .comparing(ProductionReportRow::date)
//         .reversed()
//         .thenComparing(ProductionReportRow::sku)
//         .thenComparingInt(ProductionReportRow::shift)
// );

//         return result;
//     }

//     /*  Utils * */

//     private LocalDate min(LocalDate a, LocalDate b) {
//         return a.isBefore(b) ? a : b;
//     }

//     private YearMonth minYm(YearMonth a, YearMonth b) {
//         return a.isBefore(b) ? a : b;
//     }
// }
package com.nff.NextFirstFiltrex.services.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.DailyAggReportRepository;
import com.nff.NextFirstFiltrex.repositories.MonthlyAggReportRepository;
import com.nff.NextFirstFiltrex.repositories.RawReportRepository;
import com.nff.NextFirstFiltrex.repositories.ProductionTotalsRepository;
import com.nff.NextFirstFiltrex.services.ProductionSummaryReportService;

@Service
@RequiredArgsConstructor
public class ProductionSummaryReportServiceImpl implements ProductionSummaryReportService {

    private final RawReportRepository rawRepo;
    private final DailyAggReportRepository dailyRepo;
    private final MonthlyAggReportRepository monthlyRepo;
    private final ProductionTotalsRepository totalsRepo;

    @Override
    public List<ProductionReportRow> getReport(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be <= To date");
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<ProductionReportRow> result = new ArrayList<>();

        /* raw rows for today */
        if (!from.isAfter(today) && !to.isBefore(today)) {
            result.addAll(rawRepo.todayReport(today, sku, shift));
        }

        /* daily rows up to yesterday */
        LocalDate dailyFrom = from;
        LocalDate dailyTo = min(to, yesterday);
        if (!dailyFrom.isAfter(dailyTo)) {
            result.addAll(dailyRepo.range(dailyFrom, dailyTo, sku, shift));
        }

        /* monthly rows for fully closed months */
        YearMonth reqStart = YearMonth.from(from);
        YearMonth reqEnd   = YearMonth.from(to);
        YearMonth lastFull = YearMonth.from(today.minusMonths(1));
        if (reqStart.isBefore(lastFull)) {
            YearMonth monthEnd = minYm(reqEnd, lastFull);
            result.addAll(
                monthlyRepo.range(
                    reqStart.getYear(), reqStart.getMonthValue(),
                    monthEnd.getYear(), monthEnd.getMonthValue(),
                    sku, shift
                )
            );
        }

        result.sort(
            Comparator.comparing(ProductionReportRow::date).reversed()
                      .thenComparing(ProductionReportRow::sku)
                      .thenComparingInt(ProductionReportRow::shift)
        );

        return result;
    }

    @Override
    public List<ProductionReportRow> getDailyReport(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be <= To date");
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<ProductionReportRow> result = new ArrayList<>();
        if (!from.isAfter(today) && !to.isBefore(today)) {
            result.addAll(rawRepo.todayReport(today, sku, shift));
        }
        LocalDate dailyFrom = from;
        LocalDate dailyTo = min(to, yesterday);
        if (!dailyFrom.isAfter(dailyTo)) {
            result.addAll(dailyRepo.range(dailyFrom, dailyTo, sku, shift));
        }
        result.sort(
            Comparator.comparing(ProductionReportRow::date).reversed()
                      .thenComparing(ProductionReportRow::sku)
                      .thenComparingInt(ProductionReportRow::shift)
        );
        return result;
    }

    @Override
    public List<ProductionReportRow> getWeeklyReport(
            int year,
            String sku,
            Integer shift) {

        String shiftStr = shift == null ? "ALL" : shift.toString();
        return totalsRepo.fetchWeeklyRowsForYear(year, sku, shiftStr);
    }

    @Override
    public List<ProductionReportRow> getMonthlyReport(
            int year,
            String sku,
            Integer shift) {

        String shiftStr = shift == null ? "ALL" : shift.toString();
        List<ProductionTotalsRow> rows =
                totalsRepo.fetchMonthlyTotals(year, 1, year, 12, sku, shiftStr);

        return rows.stream()
                .map(r -> new ProductionReportRow(
                        r.fromDate(),
                        r.sku(),
                        "ALL".equals(r.shift()) ? 0 :
                                Integer.parseInt(r.shift()),
                        r.totalCount(),
                        r.okCount(),
                        r.notOkCount()))
                .collect(Collectors.toList());
    }

    /* helpers */

    private LocalDate min(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? a : b;
    }

    private YearMonth minYm(YearMonth a, YearMonth b) {
        return a.isBefore(b) ? a : b;
    }
}
