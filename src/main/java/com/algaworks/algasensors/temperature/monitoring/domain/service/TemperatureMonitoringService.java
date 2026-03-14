package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogDto;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitoringService {

  private final SensorMonitoringRepository sensorMonitoringRepository;
  private final TemperatureLogRepository temperatureLogRepository;

  @Transactional
  public void processTemperatureLog(TemperatureLogDto temperatureLogDto) {
    SensorId sensorId = new SensorId(temperatureLogDto.getSensorId());
    sensorMonitoringRepository.findById(sensorId)
        .ifPresentOrElse(sensorMonitoring -> handleSensorMonitoring(sensorMonitoring, temperatureLogDto),
            () -> logIgnoredTemperature(temperatureLogDto));
  }

  private void logIgnoredTemperature(TemperatureLogDto temperatureLogDto) {
    log.info("Temperature log ignored: SensorId {} Temp {}", temperatureLogDto.getSensorId(),
        temperatureLogDto.getValue());
  }

  private void handleSensorMonitoring(SensorMonitoring sensorMonitoring, TemperatureLogDto temperatureLogDto) {
    if (sensorMonitoring.getEnabled()) {
      sensorMonitoring.setLastTemperature(temperatureLogDto.getValue());
      sensorMonitoring.setUpdatedAt(OffsetDateTime.now());
      sensorMonitoringRepository.save(sensorMonitoring);

      TemperatureLog temperatureLog = TemperatureLog.builder()
          .id(new TemperatureLogId(temperatureLogDto.getId()))
          .sensorId(sensorMonitoring.getId())
          .value(temperatureLogDto.getValue())
          .registeredAt(temperatureLogDto.getRegisteredAt())
          .build();
      temperatureLogRepository.save(temperatureLog);

      log.info("Temperature log updated: SensorId {} Temp {}", temperatureLogDto.getSensorId(),
          temperatureLogDto.getValue());
    } else {
      log.info("Sensor {} is disabled", sensorMonitoring.getId());
    }
  }
}
