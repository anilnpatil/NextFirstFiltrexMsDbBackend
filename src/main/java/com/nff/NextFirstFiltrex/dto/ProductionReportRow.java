package com.nff.NextFirstFiltrex.dto;

import java.time.LocalDate;

public record ProductionReportRow(
        LocalDate date,
        String sku,
        int shift,
        int totalCount,
        int okCount,
        int notOkCount
) {}

