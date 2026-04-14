package com.nff.NextFirstFiltrex.services;

import java.io.File;
import java.time.LocalDate;

public interface EmailService {

    void sendReportEmail(File reportFile, Integer shift, LocalDate reportDate);
}