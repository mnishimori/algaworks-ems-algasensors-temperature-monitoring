package com.algaworks.algasensors.temperature.monitoring.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sensor_alerts")
public class SensorAlert {

  @Id
  @AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "bigint"))
  private SensorId id;

  @Column(name = "max_temperature")
  private Double maxTemperature;

  @Column(name = "min_temperature")
  private Double minTemperature;

}
