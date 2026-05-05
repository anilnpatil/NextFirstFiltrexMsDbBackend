IF OBJECT_ID('dbo.filtrex_daily_agg', 'U') IS NULL
BEGIN
CREATE TABLE dbo.filtrex_daily_agg (
    production_date DATE NOT NULL,
    sku INT NOT NULL,
    shift INT NOT NULL,

    total_count INT NOT NULL,
    ok_count INT NOT NULL,
    not_ok_count INT NOT NULL,

    CONSTRAINT pk_filtrex_daily_agg
    PRIMARY KEY CLUSTERED (production_date, sku, shift)
);

CREATE INDEX idx_filtrex_daily_sku
ON dbo.filtrex_daily_agg (sku, production_date);

CREATE INDEX idx_filtrex_daily_shift
ON dbo.filtrex_daily_agg (shift, production_date);
END