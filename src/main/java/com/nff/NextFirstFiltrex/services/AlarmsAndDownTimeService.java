package com.nff.NextFirstFiltrex.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nff.NextFirstFiltrex.entities.AlarmAndDownTime;

public interface AlarmsAndDownTimeService {

  // get latest alarm
  ResponseEntity<AlarmAndDownTime> getLatestAlarm();

  //get all alarms
  List<AlarmAndDownTime> getAllAlarms();

}
