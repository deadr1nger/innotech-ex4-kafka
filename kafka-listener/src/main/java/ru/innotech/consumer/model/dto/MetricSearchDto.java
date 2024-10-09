package ru.innotech.consumer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricSearchDto {

    String key;
    String operator;
    String value;
}
