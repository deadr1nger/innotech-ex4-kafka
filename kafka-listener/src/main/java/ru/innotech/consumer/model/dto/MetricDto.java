package ru.innotech.consumer.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
@Data
public class MetricDto {

    UUID id;
    String systemName;
    LocalDateTime date;
    Map<String, Double> metrics;
}
