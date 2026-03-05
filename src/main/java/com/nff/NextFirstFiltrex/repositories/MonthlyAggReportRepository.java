package com.nff.NextFirstFiltrex.repositories;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import com.nff.NextFirstFiltrex.dto.ProductionReportRow;

@Repository
@RequiredArgsConstructor
public class MonthlyAggReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ProductionReportRow> range(
            int fromYear, int fromMonth,
            int toYear, int toMonth,
            String sku,
            Integer shift) {

        String sql = """
            SELECT
              DATEFROMPARTS(year, month, 1) AS date,
              sku,
              shift,
              total_count,
              ok_count,
              not_ok_count
            FROM filtrex_monthly_agg
            WHERE (year * 12 + month)
                  BETWEEN (? * 12 + ?) AND (? * 12 + ?)
              AND (? = 'ALL' OR sku = ?)
              AND (? IS NULL OR shift = ?)
            ORDER BY year, month
        """;

        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setInt(1, fromYear);
                    ps.setInt(2, fromMonth);
                    ps.setInt(3, toYear);
                    ps.setInt(4, toMonth);
                    ps.setString(5, sku);
                    ps.setString(6, sku);
                    ps.setObject(7, shift);
                    ps.setObject(8, shift);
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
