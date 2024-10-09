package ru.innotech.consumer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.dto.MetricSearchDto;
import ru.innotech.consumer.service.MetricSearchService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricControllerTest {

    @Mock
    private MetricSearchService metricSearchService;

    @InjectMocks
    private MetricController metricController;

    private MetricDto metricDto;
    private UUID metricId;

    @BeforeEach
    void setUp() {
        metricDto = new MetricDto();
        metricId = UUID.randomUUID();
    }

    @Test
    void findMetrics_ShouldReturnSetOfMetricDtos() {
        Set<MetricDto> expectedMetrics = Collections.singleton(metricDto);
        when(metricSearchService.findAllMetrics()).thenReturn(expectedMetrics);

        Set<MetricDto> result = metricController.findMetrics();

        assertEquals(expectedMetrics, result);
        verify(metricSearchService).findAllMetrics();
    }

    @Test
    void findMetric_ShouldReturnMetricDto_WhenIdIsValid() {
        when(metricSearchService.searchMetric(metricId)).thenReturn(metricDto);

        MetricDto result = metricController.findMetric(metricId);

        assertEquals(metricDto, result);
        verify(metricSearchService).searchMetric(metricId);
    }

    @Test
    void findMetricsByFilter_ShouldReturnSetOfMetricDtos_WhenFiltersAreProvided() {
        List<MetricSearchDto> filters = Collections.singletonList(new MetricSearchDto("key", "eq", "value"));
        Set<MetricDto> expectedMetrics = Collections.singleton(metricDto);
        when(metricSearchService.searchMetricsByFilter(filters)).thenReturn(expectedMetrics);

        Set<MetricDto> result = metricController.findMetricsByFilter(filters);

        assertEquals(expectedMetrics, result);
        verify(metricSearchService).searchMetricsByFilter(filters);
    }
}
