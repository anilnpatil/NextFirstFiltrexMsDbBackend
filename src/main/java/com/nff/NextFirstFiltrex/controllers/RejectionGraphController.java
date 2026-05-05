package com.nff.NextFirstFiltrex.controllers;

import com.nff.NextFirstFiltrex.dto.RejectionTypeProjection;
import com.nff.NextFirstFiltrex.services.RejectionGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports/rejection-types")
@RequiredArgsConstructor
public class RejectionGraphController {

    private final RejectionGraphService service;

    private static Integer normalizeFilter(Integer value) {
        return (value != null && value == 0) ? null : value;
    }

    /* ================= DAY ================= */
    @GetMapping("/day")
    public List<RejectionTypeProjection> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        return service.getDaily(
                fromDate,
                toDate,
                normalizeFilter(sku),
                normalizeFilter(shift)
        );
    }

    /* ================= WEEK ================= */
    @GetMapping("/week")
    public List<RejectionTypeProjection> weekly(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        return service.getWeekly(
                year,
                normalizeFilter(sku),
                normalizeFilter(shift)
        );
    }

    /* ================= MONTH ================= */
    @GetMapping("/month")
    public List<RejectionTypeProjection> monthly(
            @RequestParam int year,
            @RequestParam(required = false) Integer sku,
            @RequestParam(required = false) Integer shift) {

        return service.getMonthly(
                year,
                normalizeFilter(sku),
                normalizeFilter(shift)
        );
    }
}