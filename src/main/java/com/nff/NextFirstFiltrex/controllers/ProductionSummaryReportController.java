package com.nff.NextFirstFiltrex.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.services.ProductionSummaryReportService;

@RestController
@RequestMapping("/api/reports/productionSummary")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductionSummaryReportController {

    private final ProductionSummaryReportService service;

    /* ================= DAILY ================= */

    @GetMapping("/day")
    public List<ProductionSummaryReportRow> daily(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = (sku != null && sku == 0) ? null : sku;
        Integer svcShift = (shift != null && shift == 0) ? null : shift;

        return service.getDailySummaryReport(from, to, svcSku, svcShift);
    }

    /* ================= WEEK ================= */

    @GetMapping("/week")
    public List<ProductionSummaryReportRow> week(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = (sku != null && sku == 0) ? null : sku;
        Integer svcShift = (shift != null && shift == 0) ? null : shift;

        return service.getWeeklySummaryReport(year, svcSku, svcShift);
    }

    /* ================= MONTH ================= */

    @GetMapping("/month")
    public List<ProductionSummaryReportRow> month(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        Integer svcSku = (sku != null && sku == 0) ? null : sku;
        Integer svcShift = (shift != null && shift == 0) ? null : shift;

        return service.getMonthlySummaryReport(year, svcSku, svcShift);
    }
}