package com.nff.NextFirstFiltrex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nff.NextFirstFiltrex.entities.AlarmAndDownTime;
import com.nff.NextFirstFiltrex.services.AlarmsAndDownTimeService;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
public class AlarmAndDownTimeController {

    @Autowired
    private AlarmsAndDownTimeService alarmsAndDownTimeService;

    @GetMapping("/latest")
    public ResponseEntity<AlarmAndDownTime> getLatestAlarm() {
        return alarmsAndDownTimeService.getLatestAlarm();
    }

    @GetMapping("/all")
    public ResponseEntity<List<AlarmAndDownTime>> getAllAlarms() {
        List<AlarmAndDownTime> alarms = alarmsAndDownTimeService.getAllAlarms();
        return ResponseEntity.ok(alarms);
    }

}