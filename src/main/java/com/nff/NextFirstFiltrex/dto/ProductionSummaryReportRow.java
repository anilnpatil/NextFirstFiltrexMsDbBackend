package com.nff.NextFirstFiltrex.dto;

import java.time.LocalDate;
import java.util.Map;

public record ProductionSummaryReportRow(
        LocalDate date,
        String sku,
        Integer shift,
        Integer totalCount,
        Integer okCount,
        Integer notOkCount
) {
    private static final Map<Integer, String> SKU_LABELS = Map.ofEntries(
            Map.entry(0, "0) ALL"),
            Map.entry(1, "1) SP210"),
            Map.entry(2, "2) DFC Nano"),
            Map.entry(3, "3) 10\" STD MATRIKX models"),
            Map.entry(4, "4) DFC Inline RO"),
            Map.entry(5, "5) Havells carbon block"),
            Map.entry(6, "6) Ecowater078"),
            Map.entry(7, "7) Ecowater108"),
            Map.entry(8, "8) DFC Chemiblock"),
            Map.entry(9, "9) Nova family(I Nova & G nova)"),
            Map.entry(10, "10) Livpure"),
            Map.entry(11, "11) Ecowater055"),
            Map.entry(12, "12) DFC MCHPS"),
            Map.entry(13, "13) Aquatru pre"),
            Map.entry(14, "14) Aquatru post")
    );

    public static String formatSku(Integer sku) {
        return sku == null ? null : SKU_LABELS.getOrDefault(sku, sku.toString());
    }

    public static int parseSkuId(String skuLabel) {
        if (skuLabel == null) {
            return Integer.MAX_VALUE;
        }
        int idx = skuLabel.indexOf(')');
        if (idx <= 0) {
            try {
                return Integer.parseInt(skuLabel.trim());
            } catch (NumberFormatException e) {
                return Integer.MAX_VALUE;
            }
        }
        try {
            return Integer.parseInt(skuLabel.substring(0, idx).trim());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}