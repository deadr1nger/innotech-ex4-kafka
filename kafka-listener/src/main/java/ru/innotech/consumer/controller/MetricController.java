package ru.innotech.consumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.dto.MetricSearchDto;
import ru.innotech.consumer.service.MetricSearchService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricController {

    private final MetricSearchService metricSearchService;

    @GetMapping
    public Set<MetricDto> findMetrics(){
        return metricSearchService.findAllMetrics();
    }
    @GetMapping("/{id}")
    public MetricDto findMetric(@PathVariable UUID id){
        return metricSearchService.searchMetric(id);
    }

    @GetMapping("/by-filter")
    public Set<MetricDto> findMetricsByFilter(@RequestBody List<MetricSearchDto> dtos){
        return metricSearchService.searchMetricsByFilter(dtos);
    }
}
