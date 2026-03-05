package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FiltrexDailyAggregationRepository {

    private final JdbcTemplate jdbcTemplate;

    public void aggregate(LocalDate date) {

        // Use SQL Server compatible MERGE statement and CAST to date
        String sql = """
            MERGE INTO filtrex_daily_agg AS target
            USING (
              SELECT
                CAST([timestamp] AS date) AS production_date,
                sku,
                shift,
                COUNT(*) AS total_count,
                SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS ok_count,
                SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS not_ok_count
              FROM filtrex_data
              WHERE CAST([timestamp] AS date) = ?
              GROUP BY CAST([timestamp] AS date), sku, shift
            ) AS src
            ON (target.production_date = src.production_date AND target.sku = src.sku AND target.shift = src.shift)
            WHEN MATCHED THEN
              UPDATE SET
                total_count = src.total_count,
                ok_count = src.ok_count,
                not_ok_count = src.not_ok_count
            WHEN NOT MATCHED THEN
              INSERT (production_date, sku, shift, total_count, ok_count, not_ok_count)
              VALUES (src.production_date, src.sku, src.shift, src.total_count, src.ok_count, src.not_ok_count);
        """;

        jdbcTemplate.update(sql, date);
    }
}
