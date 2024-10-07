package ru.innotech.consumer.model.dto;

import lombok.Data;

@Data
public class MetricSearchDto {

    String key;
    String operator;
    String value;
}
