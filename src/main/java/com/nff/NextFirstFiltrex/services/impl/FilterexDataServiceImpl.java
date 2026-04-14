package com.nff.NextFirstFiltrex.services.impl;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nff.NextFirstFiltrex.dto.FiltrexDataResponse;
import com.nff.NextFirstFiltrex.dto.ProductionSummary;
import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.entities.FiltrexData;
import com.nff.NextFirstFiltrex.exception.ResourceNotFoundException;
import com.nff.NextFirstFiltrex.repositories.FiltrexDataJdbcRepository;
import com.nff.NextFirstFiltrex.services.FiltrexDataService;

@Service
public class FilterexDataServiceImpl implements FiltrexDataService {

    @Autowired
    private FiltrexDataJdbcRepository filtrexDataJdbcRepository;

    /* SAVE */
     @Override
    public FiltrexData saveFiltrexData(FiltrexData data) {
        filtrexDataJdbcRepository.save(data);
        return data;
    }


    /* LIVE */
    @Override
    public ResponseEntity<List<FiltrexDataResponse>> getLiveData() {

        FiltrexData last = filtrexDataJdbcRepository.getLatestRecord();

        if (last == null) {
            throw new ResourceNotFoundException("No live data found");
        }

        return ResponseEntity.ok(List.of(FiltrexDataResponse.fromEntity(last)));
    }

    /* PAGINATION */
    @Override
    public Page<FiltrexDataResponse> getDataByDateRange(
            LocalDate startDate,
            LocalDate endDate,
            Integer shift,
            Integer sku,
            Pageable pageable
    ) {

        return filtrexDataJdbcRepository.findByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay(),
                shift,
                sku,
                pageable
        ).map(FiltrexDataResponse::fromEntity);
    }

    /* latest SHIFT SUMMARY */
    @Override
    public ResponseEntity<ProductionSummary> getShiftProductionSummary(
            Integer shift,
            LocalDate date
    ) {

        LocalDateTime productionDateTime = null;
        
        if (date == null) {
            FiltrexData latest = filtrexDataJdbcRepository.getLatestRecord();
            if (latest == null) {
                throw new ResourceNotFoundException("No production data found");
            }
            productionDateTime = latest.getProductionDateTime();
            date = productionDateTime.toLocalDate();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<FiltrexData> data = filtrexDataJdbcRepository.findByDate(start, end);

        long ok = data.stream()
                .filter(d -> shift == null || shift.equals(d.getShift()))
                .filter(d -> d.getPartStatus() != null && d.getPartStatus() == 1)
                .count();

        long notOk = data.stream()
                .filter(d -> shift == null || shift.equals(d.getShift()))
                .filter(d -> d.getPartStatus() != null && d.getPartStatus() == 0)
                .count();

        long total = ok + notOk;

        Integer resolvedShift = shift;
        LocalDateTime resolvedProductionDateTime = productionDateTime;
        if (resolvedShift == null && !data.isEmpty()) {
            resolvedShift = data.stream()
                    .max(Comparator.comparing(FiltrexData::getProductionDateTime))
                    .map(FiltrexData::getShift)
                    .orElse(null);
            resolvedProductionDateTime = data.stream()
                    .max(Comparator.comparing(FiltrexData::getProductionDateTime))
                    .map(FiltrexData::getProductionDateTime)
                    .orElse(null);
        }

        final Integer finalResolvedShift = resolvedShift;
        String finalSku = data.stream()
                .filter(d -> finalResolvedShift == null || finalResolvedShift.equals(d.getShift()))
                .max(Comparator.comparing(FiltrexData::getProductionDateTime))
                .map(FiltrexData::getSku)
                .map(ProductionSummaryReportRow::formatSku)
                .orElse(null);

        ProductionSummary summary = new ProductionSummary(ok, notOk, total, resolvedShift, finalSku, resolvedProductionDateTime);

        return ResponseEntity.ok(summary);
    }

    @Override
    public ResponseEntity<byte[]> exportShiftProductionSummary(
            Integer shift,
            LocalDate date
    ) {
        ResponseEntity<ProductionSummary> summaryResponse = getShiftProductionSummary(shift, date);
        ProductionSummary summary = summaryResponse.getBody();
        if (summary == null) {
            throw new ResourceNotFoundException("Shift production summary not available");
        }

        LocalDate reportDate = date;
        if (reportDate == null) {
            FiltrexData latest = filtrexDataJdbcRepository.getLatestRecord();
            if (latest == null) {
                throw new ResourceNotFoundException("No production data found to export");
            }
            reportDate = latest.getProductionDateTime().toLocalDate();
        }

        String shiftLabel = summary.getShift() == null ? "ALL" : summary.getShift().toString();
        String fileName = String.format("shift-summary-%s-shift-%s.csv",
                reportDate, shiftLabel);
        Path exportDir = Paths.get("shift-reports").toAbsolutePath();

        try {
            Files.createDirectories(exportDir);

            String csv = "Date,Shift,TotalProduction,OkParts,NotOkParts\n" +
                    reportDate + "," + shiftLabel + "," + summary.getTotalProduction() + "," +
                    summary.getOkParts() + "," + summary.getNotOkParts() + "\n";

            Path exportFile = exportDir.resolve(fileName);
            Files.write(exportFile, csv.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to export shift summary report", ex);
        }
    }
   
}