package com.nff.NextFirstFiltrex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nff.NextFirstFiltrex.dto.ProductionSummary;
import com.nff.NextFirstFiltrex.entities.FiltrexData;
import com.nff.NextFirstFiltrex.services.FiltrexDataService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/filtrexdata")
@CrossOrigin(origins = "*")
public class FiltrexDataController {
    @Autowired
    private FiltrexDataService filtrexDataService;

    //to save the filtrex data
    @PostMapping("/save")
    public ResponseEntity<FiltrexData> saveFiltrexData(@RequestBody FiltrexData data) {    
    return ResponseEntity.ok(filtrexDataService.saveFiltrexData(data));
    }

   
    //get live data
    @GetMapping("/live")
    public ResponseEntity<List<FiltrexData>> getLiveData() {
        return filtrexDataService.getLiveData();
    }    

    //get by date range with pagination and sorting used for the UI data
    @GetMapping("/daterange")
    public ResponseEntity<Page<FiltrexData>> getDataByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate end,
            @RequestParam(required = false)Integer shift,
            @RequestParam(required = false) String sku,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "15")int size
    ) {

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Page<FiltrexData> data =
                filtrexDataService.getDataByDateRange(start, end, shift, sku, pageable);

        return ResponseEntity.ok(data);
    }


    //get production summary for a specific shift and date
    @GetMapping("/production-summary/shift")
    public ResponseEntity<ResponseEntity<ProductionSummary>> getShiftProductionSummary(
            @RequestParam(required = false) Integer shift,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        return ResponseEntity.ok(filtrexDataService.getShiftProductionSummary(shift, date));
    }

}
