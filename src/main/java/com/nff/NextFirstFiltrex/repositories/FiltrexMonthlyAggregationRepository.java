package com.nff.NextFirstFiltrex.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FiltrexMonthlyAggregationRepository {

    private final JdbcTemplate jdbcTemplate;

    public void aggregate(int year, int month) {

        String sql = """
            MERGE INTO filtrex_monthly_agg AS target
            USING (
              SELECT
                YEAR(production_date) AS yr,
                MONTH(production_date) AS mth,
                sku,
                shift,
                SUM(total_count) AS total_count,
                SUM(ok_count) AS ok_count,
                SUM(not_ok_count) AS not_ok_count
              FROM filtrex_daily_agg
              WHERE YEAR(production_date) = ?
                AND MONTH(production_date) = ?
              GROUP BY YEAR(production_date), MONTH(production_date), sku, shift
            ) AS src
            ON (target.[year] = src.yr AND target.[month] = src.mth AND target.sku = src.sku AND target.[shift] = src.shift)
            WHEN MATCHED THEN
              UPDATE SET
                total_count = src.total_count,
                ok_count = src.ok_count,
                not_ok_count = src.not_ok_count
            WHEN NOT MATCHED THEN
              INSERT ([year], [month], sku, [shift], total_count, ok_count, not_ok_count)
              VALUES (src.yr, src.mth, src.sku, src.shift, src.total_count, src.ok_count, src.not_ok_count);
        """;

        jdbcTemplate.update(sql, year, month);
    }
}
