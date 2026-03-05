package com.nff.NextFirstFiltrex.schedulers;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nff.NextFirstFiltrex.repositories.FiltrexDailyAggregationRepository;
import com.nff.NextFirstFiltrex.repositories.FiltrexMonthlyAggregationRepository;

import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
@EnableScheduling
public class AggregationScheduler {

    private final FiltrexDailyAggregationRepository filtrexDailyAggregationRepository;
    private final FiltrexMonthlyAggregationRepository filtrexMonthlyAggregationRepository;

    @Scheduled(cron = "0 5 0 * * *") // 12:05 AM
    public void dailyAggregation() {
        filtrexDailyAggregationRepository.aggregate(LocalDate.now().minusDays(1));
    }

    @Scheduled(cron = "0 10 0 1 * *") // 1st of month
    public void monthlyAggregation() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        filtrexMonthlyAggregationRepository.aggregate(lastMonth.getYear(), lastMonth.getMonthValue());
    }
}