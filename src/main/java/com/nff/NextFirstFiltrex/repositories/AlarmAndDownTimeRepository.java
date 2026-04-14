package com.nff.NextFirstFiltrex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nff.NextFirstFiltrex.entities.AlarmAndDownTime;

@Repository
public interface AlarmAndDownTimeRepository extends JpaRepository<AlarmAndDownTime, Long>{

  AlarmAndDownTime findTopByOrderByAlarmDateTimeDesc();

}
