package com.nff.NextFirstFiltrex.services.impl;

import com.nff.NextFirstFiltrex.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.port:0}")
    private Integer mailPort;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Value("${report.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${report.email.to:}")
    private String toAddresses;

    @Value("${report.email.subject:Filtrex shift production summary report}")
    private String emailSubject;

    @Value("${report.email.body:Please find attached the shift production summary report.}")
    private String emailBody;

    @Override
    public void sendReportEmail(File reportFile, Integer shift, LocalDate reportDate) {
        if (!emailEnabled) {
            log.warn("Report email delivery is disabled by report.email.enabled=false. File available at {}", reportFile.getAbsolutePath());
            return;
        }

        if (toAddresses == null || toAddresses.isBlank()) {
            log.warn("No report.email.to recipients configured; skipping report email delivery for shift {} on {}.", shift, reportDate);
            return;
        }

        if (mailHost == null || mailHost.isBlank() || mailHost.contains("example.com") || mailPort == null || mailPort == 0) {
            log.warn("Report email skipped because SMTP is not configured properly. spring.mail.host='{}', spring.mail.port='{}'.", mailHost, mailPort);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if (fromAddress != null && !fromAddress.isBlank()) {
                helper.setFrom(fromAddress);
            }

            helper.setTo(parseRecipients(toAddresses));
            helper.setSubject(emailSubject + " - " + reportDate + " - shift " + shift);
            helper.setText(emailBody + "\n\nReport date: " + reportDate + "\nShift: " + shift, false);

            FileSystemResource attachment = new FileSystemResource(reportFile);
            helper.addAttachment(reportFile.getName(), attachment);

            mailSender.send(message);
            log.info("Sent report email for shift {} on {} to {}", shift, reportDate, toAddresses);
        } catch (Exception ex) {
            log.warn("Failed to send report email for shift {} on {}. File available at {}. Error: {}", shift, reportDate, reportFile.getAbsolutePath(), ex.getMessage(), ex);
        }
    }

    private String[] parseRecipients(String addresses) {
        List<String> recipients = Arrays.stream(addresses.split(","))
                .map(String::trim)
                .filter(addr -> !addr.isEmpty())
                .collect(Collectors.toList());
        return recipients.toArray(new String[0]);
    }
}
