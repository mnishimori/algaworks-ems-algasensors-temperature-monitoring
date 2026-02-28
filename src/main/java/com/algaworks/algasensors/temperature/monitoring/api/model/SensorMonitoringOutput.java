package com.algaworks.algasensors.temperature.monitoring.api.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorMonitoringOutput {

  private String id;
  private Double lastTemperature;
  private OffsetDateTime updatedAt;
  private Boolean enabled;
}
