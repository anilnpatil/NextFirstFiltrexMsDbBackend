package com.nff.NextFirstFiltrex.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;

@Repository
@RequiredArgsConstructor
public class RawReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ProductionSummaryReportRow> todayReport(
            LocalDate date,
            Integer sku,
            Integer shift) {

        String sql = """
            SELECT
              production_date,
              sku,
              shift,
              COUNT(*) AS total_count,
              SUM(CASE WHEN part_status = 1 THEN 1 ELSE 0 END) AS ok_count,
              SUM(CASE WHEN part_status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
            FROM filtrex_data
            WHERE production_date = ?
              AND (? IS NULL OR sku = ?)
              AND (? IS NULL OR shift = ?)
            GROUP BY production_date, sku, shift
        """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, date);
                    ps.setObject(2, sku);
                    ps.setObject(3, sku);
                    ps.setObject(4, shift);
                    ps.setObject(5, shift);
                },
                (rs, i) -> {
                    Integer skuValue = rs.getObject("sku", Integer.class);
                    return new ProductionSummaryReportRow(
                            rs.getDate("production_date").toLocalDate(),
                            ProductionSummaryReportRow.formatSku(skuValue),
                            rs.getInt("shift"),
                            rs.getInt("total_count"),
                            rs.getInt("ok_count"),
                            rs.getInt("not_ok_count")
                    );
                }
        );
    }
}