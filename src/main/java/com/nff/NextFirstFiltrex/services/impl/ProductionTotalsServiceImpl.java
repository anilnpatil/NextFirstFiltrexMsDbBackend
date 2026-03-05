package com.nff.NextFirstFiltrex.services.impl;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.ProductionTotalsRepository;
import com.nff.NextFirstFiltrex.services.ProductionTotalsService;
import com.nff.NextFirstFiltrex.util.YearWeek;

@Service
@RequiredArgsConstructor
public class ProductionTotalsServiceImpl implements ProductionTotalsService {

    private final ProductionTotalsRepository repository;

    @Override
    public List<ProductionTotalsRow> getDailyTotals(
            LocalDate from,
            LocalDate to,
            String sku,
            String shift) {

        validateSkuShift(sku, shift);
        return repository.fetchDailyTotals(from, to, sku, shift);
    }
    

    //get weekly totals for a given year, with optional SKU and Shift filters 
    @Override
    public List<ProductionTotalsRow> getWeeklyTotals(
            int year,
            String sku,
            String shift) {

        validateSkuShift(sku, shift);

        List<ProductionReportRow> dailyRows =
                repository.fetchWeeklyRowsForYear(year, sku, shift);

        Map<YearWeek, List<ProductionReportRow>> grouped =
                dailyRows.stream()
                    .collect(Collectors.groupingBy(row -> {
                        WeekFields wf = WeekFields.ISO;
                        return new YearWeek(
                            row.date().get(wf.weekBasedYear()),
                            row.date().get(wf.weekOfWeekBasedYear())
                        );
                    }));

        List<ProductionTotalsRow> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            YearWeek yw = entry.getKey();
            List<ProductionReportRow> rows = entry.getValue();

            int total = rows.stream().mapToInt(ProductionReportRow::totalCount).sum();
            int ok = rows.stream().mapToInt(ProductionReportRow::okCount).sum();
            int notOk = rows.stream().mapToInt(ProductionReportRow::notOkCount).sum();

            LocalDate from = rows.stream()
                    .map(ProductionReportRow::date)
                    .min(LocalDate::compareTo)
                    .orElseThrow();

            LocalDate to = rows.stream()
                    .map(ProductionReportRow::date)
                    .max(LocalDate::compareTo)
                    .orElseThrow();

            result.add(new ProductionTotalsRow(
                    yw.year() + "-W" + String.format("%02d", yw.week()),
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


    @Override
    public List<ProductionTotalsRow> getMonthlyTotals(
            int year,
            String sku,
            String shift) {

        validateSkuShift(sku, shift);

        int fromYear = year;
        int fromMonth = 1;
        int toYear = year;
        int toMonth = 12;

        return repository.fetchMonthlyTotals(
                fromYear,
                fromMonth,
                toYear,
                toMonth,
                sku,
                shift
        );
    }


    private void validateSkuShift(String sku, String shift) {
        if (!StringUtils.hasText(sku) || !StringUtils.hasText(shift)) {
            throw new IllegalArgumentException("SKU and Shift must not be empty");
        }
    }
}
