package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionReportRow;

@Repository
@RequiredArgsConstructor
public class DailyAggReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ProductionReportRow> range(
            LocalDate from,
            LocalDate to,
            String sku,
            Integer shift) {

        String sql = """
            SELECT
              production_date AS date,
              sku,
              shift,
              total_count,
              ok_count,
              not_ok_count
            FROM filtrex_daily_agg
            WHERE production_date BETWEEN ? AND ?
              AND (? = 'ALL' OR sku = ?)
              AND (? IS NULL OR shift = ?)
            ORDER BY production_date
        """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, from);
                    ps.setObject(2, to);
                    ps.setString(3, sku);
                    ps.setString(4, sku);
                    ps.setObject(5, shift);
                    ps.setObject(6, shift);
                },
                (rs, i) -> new ProductionReportRow(
                        rs.getDate("date").toLocalDate(),
                        rs.getString("sku"),
                        rs.getInt("shift"),
                        rs.getInt("total_count"),
                        rs.getInt("ok_count"),
                        rs.getInt("not_ok_count")
                )
        );
    }
}
