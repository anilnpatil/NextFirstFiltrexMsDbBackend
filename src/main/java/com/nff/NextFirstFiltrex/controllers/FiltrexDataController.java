package com.nff.NextFirstFiltrex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nff.NextFirstFiltrex.dto.FiltrexDataResponse;
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

    /* SAVE DATA */

    @PostMapping("/save")
    public ResponseEntity<FiltrexData> saveFiltrexData(@RequestBody FiltrexData data) {
        return ResponseEntity.ok(filtrexDataService.saveFiltrexData(data));
    }

    /* LIVE DATA */

    @GetMapping("/live")
    public ResponseEntity<List<FiltrexDataResponse>> getLiveData() {
        return filtrexDataService.getLiveData();
    }

    /* DATE RANGE DATA */

    @GetMapping("/daterange")
    public ResponseEntity<Page<FiltrexDataResponse>> getDataByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end,

            @RequestParam(required = false) Integer shift,
            @RequestParam(required = false) Integer sku,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        
        Integer svcShift = (shift != null && shift == 0) ? null : shift;
        Integer svcSku = (sku != null && sku == 0) ? null : sku;

        PageRequest pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "productionDateTime"));

        Page<FiltrexDataResponse> data =
                filtrexDataService.getDataByDateRange(start, end, svcShift, svcSku, pageable);

        return ResponseEntity.ok(data);
    }

    /* SHIFT PRODUCTION SUMMARY */

    @GetMapping("/production-summary/shift")
    public ResponseEntity<ProductionSummary> getShiftProductionSummary(
            @RequestParam(required = false) Integer shift,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return filtrexDataService.getShiftProductionSummary(shift, date);
    }  
}