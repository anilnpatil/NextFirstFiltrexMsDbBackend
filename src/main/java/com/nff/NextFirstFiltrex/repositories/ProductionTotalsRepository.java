package com.nff.NextFirstFiltrex.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.nff.NextFirstFiltrex.dto.ProductionReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.sql.ProductionTotalsSql;

@Repository
@RequiredArgsConstructor
public class ProductionTotalsRepository {

    private final JdbcTemplate jdbcTemplate;

    /* ================= DAY ================= */
    public List<ProductionTotalsRow> fetchDailyTotals(
            LocalDate from,
            LocalDate to,
            String sku,
            String shift) {

        return jdbcTemplate.query(
            ProductionTotalsSql.DAILY_TOTALS,
            ps -> {
                // First SELECT (from aggregation table)
                ps.setObject(1, from);
                ps.setObject(2, to);
                ps.setString(3, sku);
                ps.setString(4, sku);
                ps.setString(5, shift);
                ps.setString(6, shift);
                
                // Second SELECT (from main table for today)
                ps.setObject(7, from);
                ps.setObject(8, to);
                ps.setString(9, sku);
                ps.setString(10, sku);
                ps.setString(11, shift);
                ps.setString(12, shift);
            },
            (rs, i) -> mapRow(rs, i, sku, shift)
        );
    }


    /* ================= WEEK ================= */
    public List<ProductionReportRow> fetchWeeklyRowsForYear(
            int year,
            String sku,
            String shift) {

        return jdbcTemplate.query(
            ProductionTotalsSql.WEEKLY_TOTALS,
            ps -> {
                ps.setInt(1, year);
                ps.setString(2, sku);
                ps.setString(3, sku);
                ps.setString(4, shift);
                ps.setString(5, shift);

                // live part
                ps.setInt(6, year);
                ps.setString(7, sku);
                ps.setString(8, sku);
                ps.setString(9, shift);
                ps.setString(10, shift);
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



    /* ================= MONTH ================= */
    public List<ProductionTotalsRow> fetchMonthlyTotals(
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth,
            String sku,
            String shift) {

        return jdbcTemplate.query(
            ProductionTotalsSql.MONTHLY_TOTALS_RANGE,
            ps -> {
                // Aggregation part
                ps.setInt(1, fromYear);
                ps.setInt(2, fromMonth);
                ps.setInt(3, toYear);
                ps.setInt(4, toMonth);
                ps.setString(5, sku);
                ps.setString(6, sku);
                ps.setString(7, shift);
                ps.setString(8, shift);

                // Live table part
                ps.setInt(9, fromYear);
                ps.setInt(10, fromMonth);
                ps.setInt(11, toYear);
                ps.setInt(12, toMonth);
                ps.setString(13, sku);
                ps.setString(14, sku);
                ps.setString(15, shift);
                ps.setString(16, shift);

            },
            (rs, i) -> mapRow(rs, i, sku, shift)
        );
    }


    private ProductionTotalsRow mapRow(
            ResultSet rs,
            int i,
            String sku,
            String shift) throws SQLException {

        return new ProductionTotalsRow(
            rs.getString("period_key"),
            rs.getDate("from_date").toLocalDate(),
            rs.getDate("to_date").toLocalDate(),
            sku,
            shift,
            rs.getInt("total_count"),
            rs.getInt("ok_count"),
            rs.getInt("not_ok_count")
        );
    }

}
