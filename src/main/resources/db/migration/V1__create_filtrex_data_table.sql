CREATE TABLE dbo.filtrex_data (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  production_count INT,
  status INT,
  air_flow_test_result VARCHAR(20),
  final_assembly_height FLOAT,
  top_cap_press_time FLOAT,
  top_cap_hold_time FLOAT,
  bottom_cap_press_time FLOAT,
  bottom_cap_hold_time FLOAT,
  child_part_refill_status VARCHAR(20),
  sku VARCHAR(50),
  shift INT,
  [timestamp] DATETIME2 NOT NULL,
  cycle_time FLOAT
);

CREATE INDEX idx_filtrex_timestamp ON dbo.filtrex_data ([timestamp]);
CREATE INDEX idx_filtrex_shift ON dbo.filtrex_data (shift);
CREATE INDEX idx_filtrex_sku ON dbo.filtrex_data (sku);
