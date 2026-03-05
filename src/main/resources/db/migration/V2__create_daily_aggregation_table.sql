CREATE TABLE dbo.filtrex_daily_agg (
  production_date DATE NOT NULL,
  sku VARCHAR(50) NOT NULL,
  shift INT NOT NULL,

  total_count INT NOT NULL,
  ok_count INT NOT NULL,
  not_ok_count INT NOT NULL,

  CONSTRAINT pk_filtrex_daily_agg
    PRIMARY KEY (production_date, sku, shift)
);