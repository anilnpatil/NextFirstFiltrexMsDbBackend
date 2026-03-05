CREATE TABLE dbo.filtrex_monthly_agg (
  [year] INT NOT NULL,
  [month] INT NOT NULL,
  sku VARCHAR(50) NOT NULL,
  shift INT NOT NULL,

  total_count INT NOT NULL,
  ok_count INT NOT NULL,
  not_ok_count INT NOT NULL,

  CONSTRAINT pk_filtrex_monthly_agg
    PRIMARY KEY ([year], [month], sku, shift),

  CONSTRAINT chk_filtrex_monthly_month
    CHECK ([month] BETWEEN 1 AND 12)
);