package com.nff.NextFirstFiltrex.repositories;

import com.nff.NextFirstFiltrex.entities.FiltrexData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ExportReportRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private RowMapper<FiltrexData> rowMapper = (rs, rowNum) -> mapRow(rs);

    private FiltrexData mapRow(ResultSet rs) throws SQLException {
        return FiltrexData.builder()
                .slNo(rs.getLong("sl_no"))
                .sku(rs.getInt("sku"))
                .shift(rs.getInt("shift"))
                .topCapPressAndHoldTime(rs.getInt("top_cap_press_and_hold_time"))
                .bottomCapPressAndHoldTime(rs.getInt("bottom_cap_press_and_hold_time"))
                .blockHeightValue(rs.getFloat("block_height_value"))
                .blockHeightInspectionStatus(rs.getInt("block_height_inspection_status"))
                .airFlowTestResult(rs.getInt("air_flow_test_result"))
                .partStatus(rs.getInt("part_status"))
                .cycleTime(rs.getTime("cycle_time") != null ? rs.getTime("cycle_time").toLocalTime() : null)
                .productionDateTime(rs.getTimestamp("production_date_time").toLocalDateTime())
                .clothRefillStatus(rs.getInt("cloth_refill_status"))
                .capRefillStatus(rs.getInt("cap_refill_status"))
                .glueRefillStatus(rs.getInt("glue_refill_status"))
                .build();
    }

    public List<FiltrexData> findShiftData(LocalDate date, Integer shift) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        String sql = """
                SELECT * FROM dbo.filtrex_data
                WHERE production_date_time >= :start
                  AND production_date_time < :end
                  AND shift = :shift
                ORDER BY production_date_time ASC
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start", start)
                .addValue("end", end)
                .addValue("shift", shift);

        return jdbcTemplate.query(sql, params, rowMapper);
    }
}
