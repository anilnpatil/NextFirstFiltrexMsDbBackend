package com.nff.NextFirstFiltrex.services.impl;

import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.entities.FiltrexData;
import com.nff.NextFirstFiltrex.repositories.ExportReportRepository;
import com.nff.NextFirstFiltrex.services.EmailService;
import com.nff.NextFirstFiltrex.services.ExportReportService;
import com.nff.NextFirstFiltrex.services.ProductionSummaryReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportReportServiceImpl implements ExportReportService {

    private static final Logger log = LoggerFactory.getLogger(ExportReportServiceImpl.class);
    private final ExportReportRepository exportReportRepository;
    private final EmailService emailService;
    private final ProductionSummaryReportService productionSummaryReportService;

    @Override
    public java.io.File generateReportIfMissing(Integer shift, LocalDate date) {
        LocalDate reportDate = date == null ? LocalDate.now() : date;
        Path exportDir = Paths.get("shift-reports").toAbsolutePath();
        Path reportFile = resolveReportPath(exportDir, reportDate, shift);

        if (Files.exists(reportFile)) {
            log.info("Report already exists: {}", reportFile);
            return reportFile.toFile();
        }

        // Get summary data grouped by SKU
        List<ProductionSummaryReportRow> summaryData = productionSummaryReportService.getDailySummaryReport(reportDate, reportDate, null, shift);

        // Get detailed data for the details sheet
        List<FiltrexData> details = exportReportRepository.findShiftData(reportDate, shift);

        Path reportDirectory = reportFile.getParent();
        try {
            Files.createDirectories(reportDirectory);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to create report directory", ex);
        }

        createExcelReport(reportFile, reportDate, shift, summaryData, details);

        log.info("Generated report: {}", reportFile);
        try {
            emailService.sendReportEmail(reportFile.toFile(), shift, reportDate);
        } catch (Exception ex) {
            log.warn("Report generated but email delivery failed for shift {} on {}. File available at {}", shift, reportDate, reportFile, ex);
        }
        return reportFile.toFile();
    }

    private Path resolveReportPath(Path directory, LocalDate reportDate, Integer shift) {
       String folderName = switch (shift) {
        case 1 -> "shift-1";
        case 2 -> "shift-2";
        case 3 -> "shift-3";
        default -> "shift-" + shift;
    };
        String fileName = String.format("%s(%d).xlsx", reportDate, shift);
        return directory.resolve(folderName).resolve(fileName);
    }

    private void createExcelReport(Path reportFile,
                                   LocalDate reportDate,
                                   Integer shift,
                                   List<ProductionSummaryReportRow> summaryData,
                                   List<FiltrexData> details) {
        try (Workbook workbook = new XSSFWorkbook()) {
            createSummarySheet(workbook, reportDate, shift, summaryData);
            createDetailsSheet(workbook, details);

            try (OutputStream outputStream = Files.newOutputStream(reportFile,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                workbook.write(outputStream);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to create Excel report", ex);
        }
    }

    private void createSummarySheet(Workbook workbook,
                                    LocalDate reportDate,
                                    Integer shift,
                                    List<ProductionSummaryReportRow> summaryData) {
        Sheet sheet = workbook.createSheet("Summary");

        // Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("SKU");
        header.createCell(2).setCellValue("Shift");
        header.createCell(3).setCellValue("Total Count");
        header.createCell(4).setCellValue("OK Count");
        header.createCell(5).setCellValue("Not OK Count");

        // Data rows
        int rowIndex = 1;
        for (ProductionSummaryReportRow row : summaryData) {
            Row excelRow = sheet.createRow(rowIndex++);
            excelRow.createCell(0).setCellValue(row.date().toString());
            excelRow.createCell(1).setCellValue(row.sku());
            excelRow.createCell(2).setCellValue(row.shift());
            excelRow.createCell(3).setCellValue(row.totalCount());
            excelRow.createCell(4).setCellValue(row.okCount());
            excelRow.createCell(5).setCellValue(row.notOkCount());
        }

        // Auto-size columns
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDetailsSheet(Workbook workbook, List<FiltrexData> details) {
        Sheet sheet = workbook.createSheet("Details");
        Row header = sheet.createRow(0);
        String[] columns = {
                "Sl No",
                "SKU",
                "Shift",
                "Production DateTime",
                "Part Status",
                "Cycle Time",
                "Top Cap Press And Hold Time",
                "Bottom Cap Press And Hold Time",
                "Block Height Value",
                "Block Height Inspection Status",
                "Air Flow Test Result",
                "Cloth Refill Status",
                "Cap Refill Status",
                "Glue Refill Status"
        };
        for (int index = 0; index < columns.length; index++) {
            header.createCell(index).setCellValue(columns[index]);
        }

        int rowIndex = 1;
        for (FiltrexData rowData : details) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            row.createCell(cellIndex++).setCellValue(safeLong(rowData.getSlNo()));
            row.createCell(cellIndex++).setCellValue(safeInt(rowData.getSku()));
            row.createCell(cellIndex++).setCellValue(safeInt(rowData.getShift()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getProductionDateTime()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getPartStatus()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getCycleTime()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getTopCapPressAndHoldTime()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getBottomCapPressAndHoldTime()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getBlockHeightValue()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getBlockHeightInspectionStatus()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getAirFlowTestResult()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getClothRefillStatus()));
            row.createCell(cellIndex++).setCellValue(valueOrEmpty(rowData.getCapRefillStatus()));
            row.createCell(cellIndex).setCellValue(valueOrEmpty(rowData.getGlueRefillStatus()));
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private String valueOrEmpty(Object value) {
        return value == null ? "" : value.toString();
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
