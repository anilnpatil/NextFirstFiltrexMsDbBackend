package com.nff.NextFirstFiltrex.repositories.sql;

public final class ProductionTotalsSql {

    private ProductionTotalsSql() {}

    /* ================= DAY ================= */
    public static final String DAILY_TOTALS = """
        SELECT
          from_date,
          to_date,
          CONVERT(varchar(10), from_date, 23) AS period_key,
          SUM(total_count) AS total_count,
          SUM(ok_count) AS ok_count,
          SUM(not_ok_count) AS not_ok_count
        FROM (
          -- Fetch from aggregation table for dates before today
          SELECT
            production_date AS from_date,
            production_date AS to_date,
            total_count,
            ok_count,
            not_ok_count,
            sku,
            shift
          FROM filtrex_daily_agg
          WHERE production_date < CAST(GETDATE() AS DATE)
            AND production_date BETWEEN ? AND ?
            AND (? = 'ALL' OR sku = ?)
            AND (? = 'ALL' OR CAST(shift AS varchar) = ?)

          UNION ALL

          -- Fetch from main table for today
          SELECT
            CAST(timestamp AS DATE) AS from_date,
            CAST(timestamp AS DATE) AS to_date,
            CASE status WHEN 1 THEN 1 ELSE 0 END AS total_count,
            CASE status WHEN 1 THEN 1 ELSE 0 END AS ok_count,
            CASE status WHEN 1 THEN 0 ELSE 1 END AS not_ok_count,
            sku,
            CAST(shift AS varchar) AS shift
          FROM filtrex_data
          WHERE CAST(timestamp AS DATE) = CAST(GETDATE() AS DATE)
            AND CAST(timestamp AS DATE) BETWEEN ? AND ?
            AND (? = 'ALL' OR sku = ?)
            AND (? = 'ALL' OR CAST(shift AS varchar) = ?)
        ) AS combined_data
        GROUP BY from_date, to_date
        ORDER BY from_date
    """;

    /* ================= WEEK ================= */
    // public static final String WEEKLY_TOTALS = """
    //     SELECT
    //         production_date,
    //         sku,
    //         shift,
    //         total_count,
    //         ok_count,
    //         not_ok_count
    //     FROM filtrex_daily_agg
    //     WHERE YEAR(production_date) = ?
    //       AND (? = 'ALL' OR sku = ?)
    //       AND (? = 'ALL' OR CAST(shift AS CHAR) = ?)
    //     ORDER BY production_date
    // """;

    public static final String WEEKLY_TOTALS = """
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
        AND (? = 'ALL' OR sku = ?)
        AND (? = 'ALL' OR CAST(shift AS CHAR) = ?)

      UNION ALL

      -- Today's live data from filtrex_data
      SELECT
          CAST([timestamp] AS DATE) AS production_date,
          sku,
          shift,
          COUNT(*) AS total_count,
          SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS ok_count,
          SUM(CASE WHEN status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
      FROM dbo.filtrex_data
      WHERE CAST([timestamp] AS DATE) = CAST(GETDATE() AS DATE)
        AND YEAR([timestamp]) = ?
        AND (? = 'ALL' OR sku = ?)
        AND (? = 'ALL' OR CAST(shift AS CHAR) = ?)
      GROUP BY CAST([timestamp] AS DATE), sku, shift

      ORDER BY production_date
  """;


    /* ================= MONTH ================= */
    public static final String MONTHLY_TOTALS_RANGE = """
      -- Completed months from aggregation table
      SELECT
          CAST(year AS varchar(4)) + '-' + RIGHT('00' + CAST(month AS varchar(2)), 2) AS period_key,
          DATEFROMPARTS(year, month, 1) AS from_date,
          EOMONTH(DATEFROMPARTS(year, month, 1)) AS to_date,
          SUM(total_count) AS total_count,
          SUM(ok_count) AS ok_count,
          SUM(not_ok_count) AS not_ok_count
      FROM filtrex_monthly_agg
      WHERE (year * 12 + month)
            BETWEEN (? * 12 + ?) AND (? * 12 + ?)
        AND (? = 'ALL' OR sku = ?)
        AND (? = 'ALL' OR CAST(shift AS varchar) = ?)
      GROUP BY year, month

      UNION ALL

      -- Current running month from live table
      SELECT
          CAST(YEAR([timestamp]) AS varchar(4)) + '-' 
              + RIGHT('00' + CAST(MONTH([timestamp]) AS varchar(2)), 2) AS period_key,
          DATEFROMPARTS(YEAR([timestamp]), MONTH([timestamp]), 1) AS from_date,
          MAX(CAST([timestamp] AS DATE)) AS to_date,
          COUNT(*) AS total_count,
          SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS ok_count,
          SUM(CASE WHEN status <> 1 THEN 1 ELSE 0 END) AS not_ok_count
      FROM dbo.filtrex_data
      WHERE (YEAR([timestamp]) * 12 + MONTH([timestamp]))
            BETWEEN (? * 12 + ?) AND (? * 12 + ?)
        AND YEAR([timestamp]) = YEAR(GETDATE())
        AND MONTH([timestamp]) = MONTH(GETDATE())
        AND (? = 'ALL' OR sku = ?)
        AND (? = 'ALL' OR CAST(shift AS varchar) = ?)
      GROUP BY YEAR([timestamp]), MONTH([timestamp])

      ORDER BY from_date
  """;

}
