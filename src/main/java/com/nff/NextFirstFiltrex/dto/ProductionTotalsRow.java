package com.nff.NextFirstFiltrex.dto;

import java.time.LocalDate;

public record ProductionTotalsRow(
        String periodKey,      // 2026-01-29 | 2026-W03 | 2026-01
        LocalDate fromDate,
        LocalDate toDate,
        String sku,             // ALL or specific
        String shift,           // ALL or 1/2/3
        int totalCount,
        int okCount,
        int notOkCount
) {}
