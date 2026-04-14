package com.nff.NextFirstFiltrex.schedulers;

import java.io.File;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nff.NextFirstFiltrex.services.ExportReportService;

@Service
public class ExportReportScheduler {

    @Autowired
    private ExportReportService exportReportService;

    // 🕑 SHIFT 1 END → 14:00
    @Scheduled(cron = "1 44 16 * * ?")   
    public void exportShift1() {
        LocalDate today = LocalDate.now();
        exportReportForShift(1, today);
    }

    // 🕙 SHIFT 2 END → 22:00
    @Scheduled(cron = "1 5 22 * * ?")
    public void exportShift2() {
        LocalDate today = LocalDate.now();
        exportReportForShift(2, today);
    }

    // 🌙 SHIFT 3 END → 06:00 (next day)
    @Scheduled(cron = "1 5 6 * * ?")
    public void exportShift3() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        exportReportForShift(3, yesterday);
    }

    private void exportReportForShift(int shift, LocalDate date) {
        try {
            File reportFile = exportReportService.generateReportIfMissing(shift, date);
            if (reportFile.exists()) {
                System.out.println("✅ Shift " + shift + " report ready: " + reportFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("⚠️ Failed to export report for shift " + shift + " on " + date + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}