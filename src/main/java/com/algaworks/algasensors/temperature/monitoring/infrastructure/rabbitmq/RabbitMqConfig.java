package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  public static final String TEMPERATURE_PROCESSING_TEMPERATURE_RECEIVED_V_1_E = "temperature-processing.temperature-received.v1.e";
  private static final String PROCESS_TEMPERATURE_V_1 = "temperature-monitoring.process-temperature.v1";
  public static final String TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q = PROCESS_TEMPERATURE_V_1 + ".q";
  public static final String TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_DLQ = PROCESS_TEMPERATURE_V_1 + ".dlq";
  public static final String TEMPERATURE_MONITORING_ALERTING_V_1_Q = "temperature-monitoring.alerting.v1.q";

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public Queue queueTemperatureMonitoring() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", "");
    args.put("x-dead-letter-routing-key", TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_DLQ);
    return QueueBuilder.durable(TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q).withArguments(args).build();
  }

  @Bean
  public Queue deadLetterQueueTemperatureMonitoring() {
    return QueueBuilder.durable(TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_DLQ).build();
  }

  @Bean
  public Queue queueAlerting() {
    return QueueBuilder.durable(TEMPERATURE_MONITORING_ALERTING_V_1_Q).build();
  }

  public FanoutExchange fanoutExchange() {
    return ExchangeBuilder.fanoutExchange(TEMPERATURE_PROCESSING_TEMPERATURE_RECEIVED_V_1_E).build();
  }

  @Bean
  public Binding bindingTemperatureMonitoring() {
    return BindingBuilder.bind(queueTemperatureMonitoring()).to(fanoutExchange());
  }

  @Bean
  public Binding bindingAlerting() {
    return BindingBuilder.bind(queueAlerting()).to(fanoutExchange());
  }
}
