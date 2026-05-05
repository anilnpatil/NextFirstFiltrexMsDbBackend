package com.nff.NextFirstFiltrex.dto;

import com.nff.NextFirstFiltrex.entities.FiltrexData;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FiltrexDataResponse(
        Long slNo,
        String sku,
        Integer shift,
        Integer topCapPressAndHoldTime,
        Integer bottomCapPressAndHoldTime,
        Float blockHeightValue,
        Integer blockHeightInspectionStatus,
        Integer airFlowTestResult,
        Integer partStatus,
        Float cycleTime,
        LocalDateTime productionDateTime,
        LocalDate productionDate,
        Integer clothRefillStatus,
        Integer capRefillStatus,
        Integer glueRefillStatus
) {

    public static FiltrexDataResponse fromEntity(FiltrexData entity) {
        return new FiltrexDataResponse(
                entity.getSlNo(),
                ProductionSummaryReportRow.formatSku(entity.getSku()),
                entity.getShift(),
                entity.getTopCapPressAndHoldTime(),
                entity.getBottomCapPressAndHoldTime(),
                entity.getBlockHeightValue(),
                entity.getBlockHeightInspectionStatus(),
                entity.getAirFlowTestResult(),
                entity.getPartStatus(),
                entity.getCycleTime(),
                entity.getProductionDateTime(),
                entity.getProductionDate(),
                entity.getClothRefillStatus(),
                entity.getCapRefillStatus(),
                entity.getGlueRefillStatus()
        );
    }
}
