package com.nff.NextFirstFiltrex.services;

import java.io.File;
import java.time.LocalDate;

public interface ExportReportService {

    File generateReportIfMissing(Integer shift, LocalDate date);
}