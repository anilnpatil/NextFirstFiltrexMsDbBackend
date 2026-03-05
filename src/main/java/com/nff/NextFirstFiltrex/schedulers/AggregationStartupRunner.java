package com.nff.NextFirstFiltrex.schedulers;

import java.time.LocalDate;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nff.NextFirstFiltrex.repositories.FiltrexDailyAggregationRepository;
import com.nff.NextFirstFiltrex.repositories.FiltrexMonthlyAggregationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AggregationStartupRunner {

    private final FiltrexDailyAggregationRepository filtrexDailyAggregationRepository;
    private final FiltrexMonthlyAggregationRepository filtrexMonthlyAggregationRepository;
    private final JdbcTemplate jdbc;

    @EventListener(ApplicationReadyEvent.class)
    public void catchUp() {

        LocalDate lastAgg =
            jdbc.queryForObject(
              "SELECT MAX(production_date) FROM filtrex_daily_agg",
              LocalDate.class
            );

        if (lastAgg == null) {
            lastAgg = LocalDate.now().minusDays(30); // safety
        }

        LocalDate yesterday = LocalDate.now().minusDays(1);

        while (!lastAgg.isAfter(yesterday)) {
            filtrexDailyAggregationRepository.aggregate(lastAgg);
            lastAgg = lastAgg.plusDays(1);
        }

        // Monthly catch-up
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        filtrexMonthlyAggregationRepository.aggregate(lastMonth.getYear(), lastMonth.getMonthValue());
    }
}
