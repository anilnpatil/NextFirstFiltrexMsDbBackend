package com.nff.NextFirstFiltrex.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.services.ProductionTotalsService;

@RestController
@RequestMapping("/api/reports/production/totals")
@RequiredArgsConstructor
public class ProductionTotalsController {

    private final ProductionTotalsService service;

    private static Integer normalizeFilter(Integer value) {
        return (value != null && value == 0) ? null : value;
    }

    /* ================= DAILY ================= */

    @GetMapping("/day")
    public List<ProductionTotalsRow> daily(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = normalizeFilter(sku);
        Integer svcShift = normalizeFilter(shift);
        return service.getDailyTotals(fromDate, toDate, svcSku, svcShift);
        
    }

    /* ================= WEEK ================= */

    @GetMapping("/week")
    public List<ProductionTotalsRow> weekly(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = normalizeFilter(sku);
        Integer svcShift = normalizeFilter(shift);
        return service.getWeeklyTotals(year, svcSku, svcShift);
    }

    /* ================= MONTH ================= */

    @GetMapping("/month")
    public List<ProductionTotalsRow> monthly(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = normalizeFilter(sku);
        Integer svcShift = normalizeFilter(shift);
        return service.getMonthlyTotals(year, svcSku, svcShift);
    }
}