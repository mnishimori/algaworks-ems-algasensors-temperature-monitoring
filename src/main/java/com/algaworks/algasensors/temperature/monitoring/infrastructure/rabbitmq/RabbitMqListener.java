package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogDto;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqListener {

  private final TemperatureMonitoringService temperatureMonitoringService;

  @RabbitListener(queues = RabbitMqConfig.TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q, concurrency = "2-3")
  @SneakyThrows
  public void listenTemperatureMonitoring(@Payload TemperatureLogDto temperatureLogDto) {
    temperatureMonitoringService.processTemperatureLog(temperatureLogDto);
    //Thread.sleep(Duration.ofSeconds(5));
  }

  @RabbitListener(queues = RabbitMqConfig.TEMPERATURE_MONITORING_ALERTING_V_1_Q, concurrency = "2-3")
  @SneakyThrows
  public void listenAlerting(@Payload TemperatureLogDto temperatureLogDto) {
    log.info("Alerting: SensorId {} Temperature {}", temperatureLogDto.getSensorId(), temperatureLogDto.getValue());
    //Thread.sleep(Duration.ofSeconds(5));
  }
}
