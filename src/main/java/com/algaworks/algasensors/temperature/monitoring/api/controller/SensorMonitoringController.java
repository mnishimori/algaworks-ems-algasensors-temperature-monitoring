package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
public class SensorMonitoringController {

  private final SensorMonitoringRepository sensorMonitoringRepository;

  public SensorMonitoringController(SensorMonitoringRepository sensorMonitoringRepository) {
    this.sensorMonitoringRepository = sensorMonitoringRepository;
  }

  @GetMapping
  public SensorMonitoringOutput getSensorMonitoring(@PathVariable TSID sensorId) {
    var sensorMonitoring = getMonitoring(sensorId);
    return SensorMonitoringOutput.builder().id(sensorMonitoring.getId().toString())
        .lastTemperature(sensorMonitoring.getLastTemperature()).updatedAt(sensorMonitoring.getUpdatedAt())
        .enabled(sensorMonitoring.getEnabled()).build();
  }

  private SensorMonitoring getMonitoring(TSID sensorId) {
    return sensorMonitoringRepository.findById(new SensorId(sensorId)).orElse(
        SensorMonitoring.builder()
            .id(new SensorId(sensorId))
            .enabled(false)
            .lastTemperature(null)
            .updatedAt(null)
            .build());
  }

  @PutMapping("/enable")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> enableSensor(@PathVariable TSID sensorId) {
    var sensorMonitoring = getMonitoring(sensorId);
    sensorMonitoring.setEnabled(true);
    sensorMonitoringRepository.save(sensorMonitoring);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/enable")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> disableSensor(@PathVariable TSID sensorId) {
    var sensorMonitoring = getMonitoring(sensorId);
    sensorMonitoring.setEnabled(false);
    sensorMonitoringRepository.save(sensorMonitoring);
    return ResponseEntity.noContent().build();
  }
}
