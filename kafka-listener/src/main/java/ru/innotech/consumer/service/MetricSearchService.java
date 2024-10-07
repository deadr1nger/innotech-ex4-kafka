package ru.innotech.consumer.service;

import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.dto.MetricSearchDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MetricSearchService {

    public MetricDto searchMetric(UUID id);
    public Set<MetricDto> findAllMetrics();
    public Set<MetricDto> searchMetricsByFilter(List<MetricSearchDto> dtos);

}
