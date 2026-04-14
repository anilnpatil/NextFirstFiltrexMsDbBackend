-- CREATE TABLE dbo.filtrex_monthly_agg (
--   [year] INT NOT NULL,
--   [month] INT NOT NULL,
--   sku VARCHAR(50) NOT NULL,
--   shift INT NOT NULL,

--   total_count INT NOT NULL,
--   ok_count INT NOT NULL,
--   not_ok_count INT NOT NULL,

--   CONSTRAINT pk_filtrex_monthly_agg
--     PRIMARY KEY ([year], [month], sku, shift),

--   CONSTRAINT chk_filtrex_monthly_month
--     CHECK ([month] BETWEEN 1 AND 12)
-- );

IF OBJECT_ID('dbo.filtrex_monthly_agg', 'U') IS NULL
BEGIN
CREATE TABLE dbo.filtrex_monthly_agg (
    [year] INT NOT NULL,
    [month] INT NOT NULL,
    sku INT NOT NULL,
    shift INT NOT NULL,

    total_count INT NOT NULL,
    ok_count INT NOT NULL,
    not_ok_count INT NOT NULL,

    CONSTRAINT pk_filtrex_monthly_agg
    PRIMARY KEY CLUSTERED ([year], [month], sku, shift),

    CONSTRAINT chk_filtrex_monthly_month
    CHECK ([month] BETWEEN 1 AND 12)
);

-- index for SKU monthly reports
CREATE INDEX idx_filtrex_monthly_sku
ON dbo.filtrex_monthly_agg (sku, [year], [month]);

-- index for shift monthly reports
CREATE INDEX idx_filtrex_monthly_shift
ON dbo.filtrex_monthly_agg (shift, [year], [month]);
END