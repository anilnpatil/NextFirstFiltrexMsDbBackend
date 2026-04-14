package com.nff.NextFirstFiltrex.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarm_and_downtime", schema = "dbo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmAndDownTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @Column(name = "alarm_code")
    private Integer alarmCode;

    @Column(name = "alarm_start_time")
    private Time alarmStartTime;

    @Column(name = "alarm_end_time")
    private Time alarmEndTime;

    @Column(name = "line_failure_data")
    private String lineFailureData;

    @Column(name = "down_time")
    private Time downTime;

    @Column(name = "alarm_date_time")
    private LocalDateTime alarmDateTime;
}
