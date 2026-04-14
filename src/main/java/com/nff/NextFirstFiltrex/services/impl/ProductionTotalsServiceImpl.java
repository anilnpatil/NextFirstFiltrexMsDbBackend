package com.nff.NextFirstFiltrex.services.impl;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.ProductionTotalsRepository;
import com.nff.NextFirstFiltrex.services.ProductionTotalsService;
import com.nff.NextFirstFiltrex.util.YearWeek;

@Service
@RequiredArgsConstructor
public class ProductionTotalsServiceImpl implements ProductionTotalsService {

    private final ProductionTotalsRepository repository;

    /* ================= DAILY ================= */

    @Override
    public List<ProductionTotalsRow> getDailyTotals(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift) {

        validateDateRange(from, to);
        return repository.fetchDailyTotals(from, to, sku, shift);
    }


    /* ================= WEEKLY ================= */

    @Override
    public List<ProductionTotalsRow> getWeeklyTotals(
            int year,
            Integer sku,
            Integer shift) {

        List<ProductionSummaryReportRow> dailyRows =
                repository.fetchWeeklyRowsForYear(year, sku, shift);

        record WeekBucket(YearWeek week) {
        }

        Map<WeekBucket, List<ProductionSummaryReportRow>> grouped =
                dailyRows.stream()
                        .collect(Collectors.groupingBy(row -> {
                            WeekFields wf = WeekFields.ISO;
                            return new WeekBucket(
                                    new YearWeek(
                                            row.date().get(wf.weekBasedYear()),
                                            row.date().get(wf.weekOfWeekBasedYear())
                                    )
                            );
                        }));

        List<ProductionTotalsRow> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {

            WeekBucket bucket = entry.getKey();
            List<ProductionSummaryReportRow> rows = entry.getValue();

            int total = rows.stream().mapToInt(ProductionSummaryReportRow::totalCount).sum();
            int ok = rows.stream().mapToInt(ProductionSummaryReportRow::okCount).sum();
            int notOk = rows.stream().mapToInt(ProductionSummaryReportRow::notOkCount).sum();

            LocalDate from = rows.stream()
                    .map(ProductionSummaryReportRow::date)
                    .min(LocalDate::compareTo)
                    .orElseThrow();

            LocalDate to = rows.stream()
                    .map(ProductionSummaryReportRow::date)
                    .max(LocalDate::compareTo)
                    .orElseThrow();

            result.add(new ProductionTotalsRow(
                    bucket.week().year() + "-W" + String.format("%02d", bucket.week().week()),
                    from,
                    to,
                    sku,
                    shift,
                    total,
                    ok,
                    notOk
            ));
        }

        result.sort(Comparator.comparing(ProductionTotalsRow::fromDate));
        return result;
    }


    /* ================= MONTHLY ================= */

    @Override
    public List<ProductionTotalsRow> getMonthlyTotals(
            int year,
            Integer sku,
            Integer shift) {

        int fromYear = year;
        int fromMonth = 1;
        int toYear = year;
        int toMonth = 12;

        List<ProductionTotalsRow> rows = repository.fetchMonthlyTotals(
                fromYear,
                fromMonth,
                toYear,
                toMonth,
                sku,
                shift
        );

        record MonthBucket(String periodKey, Integer sku, Integer shift) {
        }

        return rows.stream()
                .collect(Collectors.groupingBy(r -> new MonthBucket(r.periodKey(), sku, shift)))
                .entrySet().stream()
                .map(entry -> {
                    MonthBucket bucket = entry.getKey();
                    List<ProductionTotalsRow> groupedRows = entry.getValue();
                    int total = groupedRows.stream().mapToInt(ProductionTotalsRow::totalCount).sum();
                    int ok = groupedRows.stream().mapToInt(ProductionTotalsRow::okCount).sum();
                    int notOk = groupedRows.stream().mapToInt(ProductionTotalsRow::notOkCount).sum();
                    LocalDate fromDate = groupedRows.stream()
                            .map(ProductionTotalsRow::fromDate)
                            .min(LocalDate::compareTo)
                            .orElseThrow();
                    LocalDate toDate = groupedRows.stream()
                            .map(ProductionTotalsRow::toDate)
                            .max(LocalDate::compareTo)
                            .orElseThrow();
                    return new ProductionTotalsRow(
                            bucket.periodKey(),
                            fromDate,
                            toDate,
                            bucket.sku(),
                            bucket.shift(),
                            total,
                            ok,
                            notOk
                    );
                })
                .sorted(Comparator.comparing(ProductionTotalsRow::fromDate))
                .toList();
    }


    /* ================= VALIDATION ================= */

    private void validateDateRange(LocalDate from, LocalDate to) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range must not be null");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
    }
}