package ru.innotech.consumer.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Data
public class MetricDto {

    UUID id;
    String systemName;
    LocalDateTime timeStamp;
    Map<String, Double> metrics;
}
