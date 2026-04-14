IF OBJECT_ID('dbo.alarm_and_downtime', 'U') IS NULL
BEGIN
CREATE TABLE dbo.alarm_and_downtime (
    alarm_id BIGINT IDENTITY(1,1) PRIMARY KEY,

    alarm_code INT NULL,
    alarm_start_time TIME NULL,
    alarm_end_time TIME NULL,
    line_failure_data VARCHAR(255) NULL,
    down_time TIME NULL,

    alarm_date_time DATETIME2 NOT NULL
);

-- index for timeline queries
CREATE INDEX idx_alarm_date_time
ON dbo.alarm_and_downtime (alarm_date_time);
END