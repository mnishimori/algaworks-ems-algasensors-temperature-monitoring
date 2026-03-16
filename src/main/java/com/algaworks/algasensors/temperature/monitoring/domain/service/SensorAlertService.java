package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogDto;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {

  private final SensorAlertRepository sensorAlertRepository;

  @Transactional
  public void handleAlerting(TemperatureLogDto temperatureLogDto) {
    sensorAlertRepository.findById(new SensorId(temperatureLogDto.getSensorId()))
        .ifPresentOrElse(alert -> {
          if (alert.getMaxTemperature() != null
              && temperatureLogDto.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
            log.info("Alert Max Temp: SensorId {} Temp {}", temperatureLogDto.getSensorId(),
                temperatureLogDto.getValue());
          } else if (alert.getMinTemperature() != null
              && temperatureLogDto.getValue().compareTo(alert.getMinTemperature()) <= 0) {
            log.info("Alert Min Temp: SensorId {} Temp {}",
                temperatureLogDto.getSensorId(), temperatureLogDto.getValue());
          } else {
            logIgnoredAlert(temperatureLogDto);
          }
        }, () -> {
          logIgnoredAlert(temperatureLogDto);
        });
  }

  private static void logIgnoredAlert(TemperatureLogDto temperatureLogData) {
    log.info("Alert Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
  }
}
