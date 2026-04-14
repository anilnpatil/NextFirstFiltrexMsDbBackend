package com.nff.NextFirstFiltrex.repositories;

import com.nff.NextFirstFiltrex.entities.FiltrexData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class FiltrexDataJdbcRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /* ================= ROW MAPPER ================= */

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


    /* SAVE */
    public int save(FiltrexData data) {
        String sql = """
            INSERT INTO dbo.filtrex_data (
                sku, shift, top_cap_press_and_hold_time,
                bottom_cap_press_and_hold_time, block_height_value,
                block_height_inspection_status, air_flow_test_result,
                part_status, cycle_time, production_date_time,
                cloth_refill_status, cap_refill_status, glue_refill_status
            ) VALUES (:sku, :shift, :topCapPressAndHoldTime,
                :bottomCapPressAndHoldTime, :blockHeightValue,
                :blockHeightInspectionStatus, :airFlowTestResult,
                :partStatus, :cycleTime, :productionDateTime,
                :clothRefillStatus, :capRefillStatus, :glueRefillStatus)
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("sku", data.getSku())
                .addValue("shift", data.getShift())
                .addValue("topCapPressAndHoldTime", data.getTopCapPressAndHoldTime())
                .addValue("bottomCapPressAndHoldTime", data.getBottomCapPressAndHoldTime())
                .addValue("blockHeightValue", data.getBlockHeightValue())
                .addValue("blockHeightInspectionStatus", data.getBlockHeightInspectionStatus())
                .addValue("airFlowTestResult", data.getAirFlowTestResult())
                .addValue("partStatus", data.getPartStatus())
                .addValue("cycleTime", data.getCycleTime())
                .addValue("productionDateTime", data.getProductionDateTime())
                .addValue("clothRefillStatus", data.getClothRefillStatus())
                .addValue("capRefillStatus", data.getCapRefillStatus())
                .addValue("glueRefillStatus", data.getGlueRefillStatus());

        return jdbcTemplate.update(sql, params);
    }


    /* ================= LIVE DATA ================= */

    public FiltrexData getLatestRecord() {
        String sql = """
            SELECT TOP 1 * FROM dbo.filtrex_data
            ORDER BY production_date_time DESC
        """;

        List<FiltrexData> list = jdbcTemplate.query(sql, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    /* ================= DATE RANGE + PAGINATION ================= */

    public Page<FiltrexData> findByDateRange(
            LocalDateTime start,
            LocalDateTime end,
            Integer shift,
            Integer sku,
            Pageable pageable
    ) {

        String baseQuery = """
            FROM dbo.filtrex_data
            WHERE production_date_time >= :start
            AND production_date_time < :end
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);

        if (shift != null) {
            baseQuery += " AND shift = :shift";
            params.put("shift", shift);
        }

        if (sku != null) {
            baseQuery += " AND sku = :sku";
            params.put("sku", sku);
        }

        /* COUNT QUERY */
        String countSql = "SELECT COUNT(*) " + baseQuery;
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        /* DATA QUERY */
        String dataSql = """
            SELECT * 
        """ + baseQuery + """
            ORDER BY production_date_time DESC
            OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
        """;

        params.put("offset", pageable.getOffset());
        params.put("limit", pageable.getPageSize());

        List<FiltrexData> content = jdbcTemplate.query(dataSql, params, rowMapper);

        return new PageImpl<>(content, pageable, total);
    }

    /* ================= SHIFT SUMMARY ================= */

    public List<FiltrexData> findByDate(LocalDateTime start, LocalDateTime end) {

        String sql = """
            SELECT * FROM dbo.filtrex_data
            WHERE production_date_time >= ?
            AND production_date_time < ?
        """;

        return jdbcTemplate.getJdbcTemplate().query(sql, rowMapper, start, end);
    }
}