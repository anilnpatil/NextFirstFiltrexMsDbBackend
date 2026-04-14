package com.nff.NextFirstFiltrex.repositories.sql;

public final class ProductionSummarySql {


  private ProductionSummarySql() {}

/* ================= DAILY ================= */
    public static final String DAILY_SUMMARY = """
            SELECT
              production_date AS date,
              sku,
              shift,
              total_count,
              ok_count,
              not_ok_count
            FROM filtrex_daily_agg
            WHERE production_date BETWEEN ? AND ?
              AND (? IS NULL OR sku = ?)
              AND (? IS NULL OR shift = ?)
            ORDER BY production_date
        """;


  /* ================= WEEK ================= */
    public static final String WEEKLY_SUMMARY = """
      -- Historical aggregated data
      SELECT
          production_date,
          sku,
          shift,
          total_count,
          ok_count,
          not_ok_count
      FROM filtrex_daily_agg
      WHERE YEAR(production_date) = ?
        AND (? IS NULL OR sku = ?)
        AND (? IS NULL OR shift = ?)

      UNION ALL

      -- Today's live data
      SELECT
          production_date,
          sku,
          shift,
          COUNT(*) AS total_count,
          SUM(CASE WHEN part_status = 1 THEN 1 ELSE 0 END) AS ok_count,
          SUM(CASE WHEN part_status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
      FROM dbo.filtrex_data
      WHERE production_date = CAST(GETDATE() AS DATE)
        AND YEAR(production_date_time) = ?
        AND (? IS NULL OR sku = ?)
        AND (? IS NULL OR shift = ?)
      GROUP BY production_date, sku, shift
      ORDER BY production_date
  """;


    /* ================= MONTH ================= */
    public static final String MONTHLY_SUMMARY = """
      -- Completed past months
      SELECT
          CAST(year AS varchar(4)) + '-' + RIGHT('00' + CAST(month AS varchar(2)), 2) AS period_key,
          DATEFROMPARTS(year, month, 1) AS from_date,
          EOMONTH(DATEFROMPARTS(year, month, 1)) AS to_date,
          sku,
          shift,
          SUM(total_count) AS total_count,
          SUM(ok_count) AS ok_count,
          SUM(not_ok_count) AS not_ok_count
      FROM filtrex_monthly_agg
      WHERE (year * 12 + month)
            BETWEEN (? * 12 + ?) AND (? * 12 + ?)
        AND (? IS NULL OR sku = ?)
        AND (? IS NULL OR shift = ?)
        AND (year * 12 + month) < (YEAR(GETDATE()) * 12 + MONTH(GETDATE()))
      GROUP BY year, month, sku, shift

      UNION ALL

      -- Current running month totals from daily aggregation (up to yesterday)
      SELECT
          CAST(YEAR(production_date) AS varchar(4)) + '-' + RIGHT('00' + CAST(MONTH(production_date) AS varchar(2)), 2) AS period_key,
          DATEFROMPARTS(YEAR(production_date), MONTH(production_date), 1) AS from_date,
          MAX(production_date) AS to_date,
          sku,
          shift,
          SUM(total_count) AS total_count,
          SUM(ok_count) AS ok_count,
          SUM(not_ok_count) AS not_ok_count
      FROM filtrex_daily_agg
      WHERE production_date BETWEEN DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)
                                AND DATEADD(day, -1, CAST(GETDATE() AS date))
        AND (? * 12 + ?) <= (YEAR(production_date) * 12 + MONTH(production_date))
        AND (YEAR(production_date) * 12 + MONTH(production_date)) <= (? * 12 + ?)
        AND (? IS NULL OR sku = ?)
        AND (? IS NULL OR shift = ?)
      GROUP BY YEAR(production_date), MONTH(production_date), sku, shift

      UNION ALL

      -- Today's live data from raw production
      SELECT
          CAST(YEAR(production_date_time) AS varchar(4)) + '-' 
              + RIGHT('00' + CAST(MONTH(production_date_time) AS varchar(2)), 2) AS period_key,
          DATEFROMPARTS(YEAR(production_date_time), MONTH(production_date_time), 1) AS from_date,
          MAX(production_date) AS to_date,
          sku,
          shift,
          COUNT(*) AS total_count,
          SUM(CASE WHEN part_status = 1 THEN 1 ELSE 0 END) AS ok_count,
          SUM(CASE WHEN part_status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
      FROM dbo.filtrex_data
      WHERE production_date = CAST(GETDATE() AS DATE)
        AND (? * 12 + ?) <= (YEAR(production_date_time) * 12 + MONTH(production_date_time))
        AND (YEAR(production_date_time) * 12 + MONTH(production_date_time)) <= (? * 12 + ?)
        AND (? IS NULL OR sku = ?)
        AND (? IS NULL OR shift = ?)
      GROUP BY YEAR(production_date_time), MONTH(production_date_time), sku, shift

      ORDER BY from_date
  """;

}
  

