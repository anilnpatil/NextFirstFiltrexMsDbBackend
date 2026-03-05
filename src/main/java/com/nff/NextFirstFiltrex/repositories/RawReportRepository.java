package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionReportRow;

@Repository
@RequiredArgsConstructor
public class RawReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ProductionReportRow> todayReport(
            LocalDate date,
            String sku,
            Integer shift) {

        String sql = """
            SELECT
              CAST([timestamp] AS date) AS production_date,
              sku,
              shift,
              COUNT(*) AS total_count,
              SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS ok_count,
              SUM(CASE WHEN status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
            FROM filtrex_data
            WHERE CAST([timestamp] AS date) = ?
              AND (? = 'ALL' OR sku = ?)
              AND (? IS NULL OR shift = ?)
            GROUP BY CAST([timestamp] AS date), sku, shift
        """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, date);
                    ps.setString(2, sku);
                    ps.setString(3, sku);
                    ps.setObject(4, shift);
                    ps.setObject(5, shift);
                },
                (rs, i) -> new ProductionReportRow(
                        rs.getDate("production_date").toLocalDate(),
                        rs.getString("sku"),
                        rs.getInt("shift"),
                        rs.getInt("total_count"),
                        rs.getInt("ok_count"),
                        rs.getInt("not_ok_count")
                )
        );
    }
}
