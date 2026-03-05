CREATE TABLE dbo.alarm_and_downtime (
  alarm_id BIGINT IDENTITY(1,1) PRIMARY KEY,
  alarm_code INT,
  line_failure_data VARCHAR(255),
  down_time TIME,
  [timestamp] DATETIME2
);
