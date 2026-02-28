package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

  private final SensorAlertRepository sensorAlertRepository;

  public SensorAlertController(SensorAlertRepository sensorAlertRepository) {
    this.sensorAlertRepository = sensorAlertRepository;
  }

  @GetMapping
  public SensorAlertOutput get(@PathVariable TSID sensorId) {
    SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor alert configuration not found"));

    return SensorAlertOutput.builder()
        .id(sensorId)
        .maxTemperature(sensorAlert.getMaxTemperature())
        .minTemperature(sensorAlert.getMinTemperature())
        .build();
  }

  @PutMapping
  public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId, @Valid @RequestBody SensorAlertInput input) {
    SensorId sensorIdObj = new SensorId(sensorId);

    SensorAlert sensorAlert = sensorAlertRepository.findById(sensorIdObj)
        .orElse(SensorAlert.builder()
            .id(sensorIdObj)
            .build());

    sensorAlert.setMaxTemperature(input.getMaxTemperature());
    sensorAlert.setMinTemperature(input.getMinTemperature());

    sensorAlert = sensorAlertRepository.save(sensorAlert);

    return SensorAlertOutput.builder()
        .id(sensorId)
        .maxTemperature(sensorAlert.getMaxTemperature())
        .minTemperature(sensorAlert.getMinTemperature())
        .build();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable TSID sensorId) {
    SensorId sensorIdObj = new SensorId(sensorId);

    if (!sensorAlertRepository.existsById(sensorIdObj)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor alert configuration not found");
    }

    sensorAlertRepository.deleteById(sensorIdObj);
  }

}
