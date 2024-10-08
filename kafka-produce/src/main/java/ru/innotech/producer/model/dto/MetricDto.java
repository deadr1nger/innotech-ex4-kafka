package ru.innotech.producer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetricDto {

    UUID id;
    String systemName;
    LocalDateTime timeStamp;
    Map<String, Double> metrics;
}
