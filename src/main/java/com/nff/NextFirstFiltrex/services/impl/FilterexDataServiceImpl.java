package com.nff.NextFirstFiltrex.services.impl;

//import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nff.NextFirstFiltrex.dto.ProductionSummary;
import com.nff.NextFirstFiltrex.entities.FiltrexData;
import com.nff.NextFirstFiltrex.exception.ResourceNotFoundException;
import com.nff.NextFirstFiltrex.repositories.FiltrexDataRepository;
import com.nff.NextFirstFiltrex.services.FiltrexDataService;

@Service
public class FilterexDataServiceImpl implements FiltrexDataService {

    @Autowired
    private FiltrexDataRepository filtrexDataRepository;

    //save filtrex data
    @Override
    public FiltrexData saveFiltrexData(FiltrexData data) {
      return filtrexDataRepository.save(data);
    }

    //get live data
    @Override
    public ResponseEntity<List<FiltrexData>> getLiveData() {
      FiltrexData lastRecord = filtrexDataRepository.findFirstByOrderByTimestampDesc();
      if (lastRecord == null) {
        throw new ResourceNotFoundException("No live data found");
      }
      return ResponseEntity.ok(List.of(lastRecord));
    }
  
      
    //get by date range and shift optional
    @Override
    public Page<FiltrexData> getDataByDateRange(
            LocalDate startDate,
            LocalDate endDate,
            Integer shift,
            String sku,
            Pageable pageable
    ) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end date must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        LocalDate today = LocalDate.now();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime =
                !endDate.isBefore(today)
                        ? LocalDateTime.now()
                        : endDate.atTime(23, 59, 59, 999_999_999);

        return filtrexDataRepository
                .findByTimestampBetweenAndShiftOptional(
                        startDateTime,
                        endDateTime,
                        shift,
                        sku,
                        pageable
                );
    }
    
  
    //get shift production summary for a specific date and shift
    @Override
    public ResponseEntity<ProductionSummary> getShiftProductionSummary(Integer shift, java.time.LocalDate date) {
      if (shift == null) {
        FiltrexData lastRecord = filtrexDataRepository.findFirstByOrderByTimestampDesc();
        if (lastRecord == null) {
          throw new ResourceNotFoundException("No data found to determine shift");
        }
        shift = lastRecord.getShift();
      }
      final Integer finalShift = shift;
      java.time.LocalDateTime start = date.atStartOfDay();
      java.time.LocalDateTime end = date.plusDays(1).atStartOfDay();
      List<FiltrexData> data = filtrexDataRepository.findByTimestampBetween(start, end);   
      long okParts = data.stream().filter(f -> f.getShift() != null && f.getShift().equals(finalShift) && f.getStatus() != null && f.getStatus() == 1).mapToLong(f -> f.getProductionCount() != null ? f.getProductionCount() : 0).sum();
      long notOkParts = data.stream().filter(f -> f.getShift() != null && f.getShift().equals(finalShift) && f.getStatus() != null && f.getStatus() == 0).mapToLong(f -> f.getProductionCount() != null ? f.getProductionCount() : 0).sum();
      long totalProduction = okParts + notOkParts;
      ProductionSummary summary = new ProductionSummary(okParts, notOkParts, totalProduction, finalShift);
      return ResponseEntity.ok(summary);
    }

  
}
