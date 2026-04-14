IF OBJECT_ID('dbo.filtrex_data', 'U') IS NULL
BEGIN
CREATE TABLE dbo.filtrex_data (
    sl_no BIGINT IDENTITY(1,1) PRIMARY KEY,

    sku INT NULL,
    shift INT NULL,

    top_cap_press_and_hold_time INT NULL,
    bottom_cap_press_and_hold_time INT NULL,

    block_height_value FLOAT NULL,
    block_height_inspection_status INT NULL,

    air_flow_test_result INT NULL,
    part_status INT NULL,

    cycle_time TIME(7) NULL,

    production_date_time DATETIME2 NULL,

    -- computed date column for fast reporting
    production_date AS CAST(production_date_time AS DATE) PERSISTED,

    cloth_refill_status INT NULL,
    cap_refill_status INT NULL,
    glue_refill_status INT NULL
);

-- =========================
-- INDEXES
-- =========================

-- main reporting index (daily + shift + sku)
CREATE INDEX idx_filtrex_main
ON dbo.filtrex_data (production_date, shift, sku);
END


