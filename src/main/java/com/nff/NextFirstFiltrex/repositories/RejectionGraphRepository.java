package com.nff.NextFirstFiltrex.repositories;

import com.nff.NextFirstFiltrex.dto.RejectionTypeProjection;
import com.nff.NextFirstFiltrex.dto.RejectionTypeRow;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RejectionGraphRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String REJECTION_CASE =
            "CASE " +
            " WHEN block_height_inspection_status = 0 AND air_flow_test_result = 0 THEN 'Both Fail' " +
            " WHEN block_height_inspection_status = 0 THEN 'Block Height Fail' " +
            " WHEN air_flow_test_result = 0 THEN 'Air Flow Fail' " +
            "END";

    /* ================= DAY ================= */
    public List<RejectionTypeProjection> getDaily(
            String fromDate, String toDate, Integer sku, Integer shift) {

        String sql =
                "SELECT production_date AS period, " + REJECTION_CASE + " AS rejectionType, COUNT(*) AS count " +
                "FROM dbo.filtrex_data " +
                "WHERE production_date BETWEEN ? AND ? " +
                "AND part_status = 0 " +
                "AND (? IS NULL OR sku = ?) " +
                "AND (? IS NULL OR shift = ?) " +
                "GROUP BY production_date, " + REJECTION_CASE +
                " ORDER BY production_date, count DESC";

        return jdbcTemplate.query(sql,
                (rs, i) -> new RejectionTypeRow(
                        rs.getString("rejectionType"),
                        rs.getLong("count"),
                        rs.getString("period")
                ),
                fromDate, toDate,
                sku, sku,
                shift, shift
        );
    }

    /* ================= WEEK ================= */
    public List<RejectionTypeProjection> getWeekly(
            int year, Integer sku, Integer shift) {

        String sql =
                "SELECT CONCAT('Week ', DATEPART(WEEK, production_date)) AS period, " + REJECTION_CASE + " AS rejectionType, COUNT(*) AS count " +
                "FROM dbo.filtrex_data " +
                "WHERE YEAR(production_date) = ? " +
                "AND part_status = 0 " +
                "AND (? IS NULL OR sku = ?) " +
                "AND (? IS NULL OR shift = ?) " +
                "GROUP BY DATEPART(WEEK, production_date), " + REJECTION_CASE +
                " ORDER BY DATEPART(WEEK, production_date), count DESC";

        return jdbcTemplate.query(sql,
                (rs, i) -> new RejectionTypeRow(
                        rs.getString("rejectionType"),
                        rs.getLong("count"),
                        rs.getString("period")
                ),
                year,
                sku, sku,
                shift, shift
        );
    }

    /* ================= MONTH ================= */
    public List<RejectionTypeProjection> getMonthly(
            int year, Integer sku, Integer shift) {

        String sql =
                "SELECT CONCAT('Month ', MONTH(production_date)) AS period, " + REJECTION_CASE + " AS rejectionType, COUNT(*) AS count " +
                "FROM dbo.filtrex_data " +
                "WHERE YEAR(production_date) = ? " +
                "AND part_status = 0 " +
                "AND (? IS NULL OR sku = ?) " +
                "AND (? IS NULL OR shift = ?) " +
                "GROUP BY MONTH(production_date), " + REJECTION_CASE +
                " ORDER BY MONTH(production_date), count DESC";

        return jdbcTemplate.query(sql,
                (rs, i) -> new RejectionTypeRow(
                        rs.getString("rejectionType"),
                        rs.getLong("count"),
                        rs.getString("period")
                ),
                year,
                sku, sku,
                shift, shift
        );
    }
}