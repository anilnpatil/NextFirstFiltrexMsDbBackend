package com.nff.NextFirstFiltrex.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.nff.NextFirstFiltrex.dto.ProductionSummaryReportRow;
import com.nff.NextFirstFiltrex.dto.ProductionTotalsRow;
import com.nff.NextFirstFiltrex.repositories.sql.ProductionSummarySql;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductionSummaryReportRepositoryImpl implements ProductionSummaryReportRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ProductionSummaryReportRow> fetchDailySummary(
            LocalDate from,
            LocalDate to,
            Integer sku,
            Integer shift) {

        return jdbcTemplate.query(
                ProductionSummarySql.DAILY_SUMMARY,
                ps -> {
                    ps.setObject(1, from);
                    ps.setObject(2, to);
                    ps.setObject(3, sku);
                    ps.setObject(4, sku);
                    ps.setObject(5, shift);
                    ps.setObject(6, shift);
                },
                (rs, i) -> {
                    Integer skuValue = rs.getObject("sku", Integer.class);
                    return new ProductionSummaryReportRow(
                            rs.getDate("date").toLocalDate(),
                            ProductionSummaryReportRow.formatSku(skuValue),
                            rs.getInt("shift"),
                            rs.getInt("total_count"),
                            rs.getInt("ok_count"),
                            rs.getInt("not_ok_count")
                    );
                }
        );
    }
        @Override
    public List<ProductionSummaryReportRow> fetchWeeklySummary(
            int year,
            Integer sku,
            Integer shift) {

        return jdbcTemplate.query(
            ProductionSummarySql.WEEKLY_SUMMARY,
            ps -> {

                // aggregated table
                ps.setInt(1, year);
                ps.setObject(2, sku);
                ps.setObject(3, sku);
                ps.setObject(4, shift);
                ps.setObject(5, shift);

                // live table
                ps.setInt(6, year);
                ps.setObject(7, sku);
                ps.setObject(8, sku);
                ps.setObject(9, shift);
                ps.setObject(10, shift);
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

    @Override
    public List<ProductionTotalsRow> fetchMonthlySummary(
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth,
            Integer sku,
            Integer shift) {

        return jdbcTemplate.query(
            ProductionSummarySql.MONTHLY_SUMMARY,
            ps -> {

                // aggregated/monthly data
                ps.setInt(1, fromYear);
                ps.setInt(2, fromMonth);
                ps.setInt(3, toYear);
                ps.setInt(4, toMonth);
                ps.setObject(5, sku);
                ps.setObject(6, sku);
                ps.setObject(7, shift);
                ps.setObject(8, shift);

                // current month daily aggregation
                ps.setInt(9, fromYear);
                ps.setInt(10, fromMonth);
                ps.setInt(11, toYear);
                ps.setInt(12, toMonth);
                ps.setObject(13, sku);
                ps.setObject(14, sku);
                ps.setObject(15, shift);
                ps.setObject(16, shift);

                // today's live raw data
                ps.setInt(17, fromYear);
                ps.setInt(18, fromMonth);
                ps.setInt(19, toYear);
                ps.setInt(20, toMonth);
                ps.setObject(21, sku);
                ps.setObject(22, sku);
                ps.setObject(23, shift);
                ps.setObject(24, shift);
            },
            (rs, i) -> mapRow(rs)
        );
    }

    private ProductionTotalsRow mapRow(ResultSet rs) throws SQLException {
        Integer sku = rs.getObject("sku", Integer.class);
        Integer shift = rs.getObject("shift", Integer.class);
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
