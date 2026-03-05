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

    @GetMapping("/day")
    public List<ProductionTotalsRow> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(defaultValue = "ALL") String shift) {

        return service.getDailyTotals(fromDate, toDate, sku, shift);
    }

    @GetMapping("/week")
    public List<ProductionTotalsRow> weekly(
            @RequestParam int year,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(defaultValue = "ALL") String shift) {

        return service.getWeeklyTotals(year, sku, shift);
    }

    @GetMapping("/month")
    public List<ProductionTotalsRow> monthly(
            @RequestParam int year,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(defaultValue = "ALL") String shift) {

        return service.getMonthlyTotals(year, sku, shift);
    }
}
