package com.nff.NextFirstFiltrex.services;

import com.nff.NextFirstFiltrex.dto.FiltrexDataResponse;
import com.nff.NextFirstFiltrex.dto.ProductionSummary;
import com.nff.NextFirstFiltrex.entities.FiltrexData;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FiltrexDataService {

    FiltrexData saveFiltrexData(FiltrexData data);

    ResponseEntity<List<FiltrexDataResponse>> getLiveData();

    Page<FiltrexDataResponse> getDataByDateRange(
            LocalDate start,
            LocalDate end,
            Integer shift,
            Integer sku,
            Pageable pageable
    );

    ResponseEntity<ProductionSummary> getShiftProductionSummary(
            Integer shift,
            LocalDate date
    );

    ResponseEntity<byte[]> exportShiftProductionSummary(
            Integer shift,
            LocalDate date
    );
}