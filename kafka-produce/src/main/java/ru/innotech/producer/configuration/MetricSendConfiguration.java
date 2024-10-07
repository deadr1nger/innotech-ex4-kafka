package ru.innotech.producer.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import ru.innotech.producer.model.dto.MetricDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MetricSendConfiguration {
    private final MetricsEndpoint metricsEndpoint;
    private final KafkaTemplate<String, MetricDto> kafkaTemplate;
    @Value("${spring.kafka.topic}")
    private String topic;
    @Value("${spring.application.name}")
    public String systemName;
    @Scheduled(fixedRate = 5000)
    public void send() {
        MetricDto metricDto = getMetrics();
        kafkaTemplate.send(topic, metricDto);
    }
    public MetricDto getMetrics() {
        Map<String, Double> metricsMap = new HashMap<>();
        metricsEndpoint.listNames().getNames().forEach(metricName -> metricsEndpoint.metric(metricName, null).getMeasurements().forEach(measurement ->
            metricsMap.put(metricName + "." + measurement.getStatistic().name(), measurement.getValue())));
        return new MetricDto(UUID.randomUUID(), systemName, LocalDateTime.now(), metricsMap);
    }
}
