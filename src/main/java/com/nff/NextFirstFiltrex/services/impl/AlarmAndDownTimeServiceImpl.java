package com.nff.NextFirstFiltrex.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nff.NextFirstFiltrex.entities.AlarmAndDownTime;
import com.nff.NextFirstFiltrex.services.AlarmsAndDownTimeService;
import com.nff.NextFirstFiltrex.repositories.AlarmAndDownTimeRepository;
import com.nff.NextFirstFiltrex.exception.ResourceNotFoundException;

@Service
public class AlarmAndDownTimeServiceImpl implements AlarmsAndDownTimeService {

  @Autowired
  private AlarmAndDownTimeRepository alarmAndDownTimeRepository;

  @Override
  public ResponseEntity<AlarmAndDownTime> getLatestAlarm() {
    AlarmAndDownTime latestAlarm = alarmAndDownTimeRepository.findTopByOrderByTimestampDesc();
    if (latestAlarm == null) {
      throw new ResourceNotFoundException("No latest alarm data found");
    }
    return ResponseEntity.ok(latestAlarm);
  }

  @Override
  public List<AlarmAndDownTime> getAllAlarms() {
    return alarmAndDownTimeRepository.findAll();
  }

}
