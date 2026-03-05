package com.nff.NextFirstFiltrex.services;
import com.nff.NextFirstFiltrex.dto.ProductionSummary;
import com.nff.NextFirstFiltrex.entities.FiltrexData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FiltrexDataService {
  
  //save filtrex data
  FiltrexData saveFiltrexData(FiltrexData data);

  //geta live data
  ResponseEntity<List<FiltrexData>> getLiveData();
  
  //get by date range and shif optional
  Page<FiltrexData> getDataByDateRange(
          LocalDate start,
          LocalDate end,
          Integer shift,
          String sku,
          Pageable pageable
  );
  
  //get shift production summary for a specific date (date only)
  public ResponseEntity<ProductionSummary> getShiftProductionSummary(Integer shift, java.time.LocalDate date);

}

