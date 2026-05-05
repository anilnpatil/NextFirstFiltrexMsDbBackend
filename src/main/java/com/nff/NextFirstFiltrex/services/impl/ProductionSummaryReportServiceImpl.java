package com.nff.NextFirstFiltrex.services.impl;

import java.time.LocalDate;
// import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.ProductionSummaryReportRepository;
import com.nff.NextFirstFiltrex.repositories.RawReportRepository;
import com.nff.NextFirstFiltrex.services.ProductionSummaryReportService;

@Service
@RequiredArgsConstructor
public class ProductionSummaryReportServiceImpl implements ProductionSummaryReportService {

    private final RawReportRepository rawRepo;   
    private final ProductionSummaryReportRepository productionSummaryRepo;
    @Override
    public List<ProductionSummaryReportRow> getDailySummaryReport(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be <= To date");
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<ProductionSummaryReportRow> result = new ArrayList<>();
        if (!from.isAfter(today) && !to.isBefore(today)) {
            result.addAll(rawRepo.todayReport(today, sku, shift));
        }
        LocalDate dailyFrom = from;
        LocalDate dailyTo = min(to, yesterday);
        if (!dailyFrom.isAfter(dailyTo)) {
            result.addAll(productionSummaryRepo.fetchDailySummary(dailyFrom, dailyTo, sku, shift));
        }
        result.sort(
            Comparator.comparing(ProductionSummaryReportRow::date).reversed()
                      .thenComparingInt(r -> ProductionSummaryReportRow.parseSkuId(r.sku()))
                      .thenComparingInt(ProductionSummaryReportRow::shift)
        );
        return result;
    }

    @Override
    public List<ProductionSummaryReportRow> getWeeklySummaryReport(
            int year,
            Integer sku,
            Integer shift) {        
        return productionSummaryRepo.fetchWeeklySummary(year, sku, shift);
    }


    @Override
    public List<ProductionSummaryReportRow> getMonthlySummaryReport(
            int year,
            Integer sku,
            Integer shift) {

        List<ProductionTotalsRow> rows = productionSummaryRepo.fetchMonthlySummary(year, 1, year, 12, sku, shift);

        return rows.stream()
                .map(r -> new ProductionSummaryReportRow(
                        r.fromDate(),
                        ProductionSummaryReportRow.formatSku(r.sku()),
                        r.shift(),
                        r.totalCount(),
                        r.okCount(),
                        r.notOkCount()))
                .collect(Collectors.toList());
    }

    /* helpers */
    private LocalDate min(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? a : b;
    }    
}
