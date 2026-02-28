package com.algaworks.algasensors.temperature.monitoring.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorAlertInput {

  @NotNull(message = "Sensor ID is required")
  private Long sensorId;

  private Double maxTemperature;

  private Double minTemperature;

}
