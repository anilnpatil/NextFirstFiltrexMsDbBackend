package com.nff.NextFirstFiltrex.dto;

import java.time.LocalDate;

public record ProductionTotalsRow(
        String periodKey,      // 2026-01-29 | 2026-W03 | 2026-01
        LocalDate fromDate,
        LocalDate toDate,
        Integer sku,           
        Integer shift,         
        int totalCount,
        int okCount,
        int notOkCount
) {}