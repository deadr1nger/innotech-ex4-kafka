package ru.innotech.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.mapper.MetricMapper;
import ru.innotech.consumer.repository.MetricRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricServiceImpl implements MetricService{
    private final MetricRepository metricRepository;
    private final ObjectMapper objectMapper;
    private final MetricMapper metricMapper;
    @Override
    @KafkaListener(topics = "${spring.kafka.topic}")
    public void saveMetric(@Payload String message) {
        try {
            MetricDto convertedDto = objectMapper.readValue(message, MetricDto.class);
            metricRepository.save(metricMapper.metricDtoToEntity(convertedDto));
        } catch (JsonProcessingException e) {
            log.error("Error while during mapping message: {}", message);
            log.error(e.getMessage());
            e.getStackTrace();
        }
    }
}
