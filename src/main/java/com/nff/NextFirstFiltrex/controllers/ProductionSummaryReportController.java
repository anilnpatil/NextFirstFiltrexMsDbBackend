// package com.nff.NextFirstFiltrex.controllers;

// import java.time.LocalDate;
// import java.util.List;

// import org.springframework.web.bind.annotation.*;

// import lombok.RequiredArgsConstructor;
// import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
// import com.nff.NextFirstFiltrex.services.impl.ProductionReportServiceImpl;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


// @RestController
// @RequestMapping("/api/reports/production")
// @CrossOrigin(origins = "*")
// @RequiredArgsConstructor
// public class ProductionReportController {

//     private final ProductionReportServiceImpl service;

//     @GetMapping
//     public List<ProductionReportRow> report(
//             @RequestParam LocalDate from,
//             @RequestParam LocalDate to,
//             @RequestParam(defaultValue = "ALL") String sku,
//             @RequestParam(required = false) Integer shift) {
             
//         return service.getReport(from, to, sku, shift);
//     }   
    
// }
package com.nff.NextFirstFiltrex.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
import com.nff.NextFirstFiltrex.services.ProductionSummaryReportService;

@RestController
@RequestMapping("/api/reports/production")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductionSummaryReportController {

    private final ProductionSummaryReportService service;

    @GetMapping
    public List<ProductionReportRow> report(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(required = false) Integer shift) {

        return service.getReport(from, to, sku, shift);
    }

    /* new specialised endpoints */

    @GetMapping("/day")
    public List<ProductionReportRow> daily(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(required = false) Integer shift) {

        return service.getDailyReport(from, to, sku, shift);
    }

    @GetMapping("/week")
    public List<ProductionReportRow> week(
            @RequestParam int year,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(required = false) Integer shift) {

        return service.getWeeklyReport(year, sku, shift);
    }

    @GetMapping("/month")
    public List<ProductionReportRow> month(
            @RequestParam int year,
            @RequestParam(defaultValue = "ALL") String sku,
            @RequestParam(required = false) Integer shift) {

        return service.getMonthlyReport(year, sku, shift);
    }
}