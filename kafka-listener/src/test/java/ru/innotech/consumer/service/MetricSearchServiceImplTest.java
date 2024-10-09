package ru.innotech.consumer.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.dto.MetricSearchDto;
import ru.innotech.consumer.model.entity.MetricEntity;
import ru.innotech.consumer.model.mapper.MetricMapper;
import ru.innotech.consumer.repository.MetricRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class MetricSearchServiceImplTest {

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private MetricMapper metricMapper;

    @InjectMocks
    private MetricSearchServiceImpl metricSearchService;

    private UUID metricId;
    private MetricEntity metricEntity;
    private MetricDto metricDto;

    @BeforeEach
    void setUp() {
        metricId = UUID.randomUUID();
        metricEntity = new MetricEntity();
        metricDto = new MetricDto();
    }

    @Test
    void searchMetric_ShouldReturnMetricDto_WhenMetricExists() {
        when(metricRepository.findById(metricId)).thenReturn(Optional.of(metricEntity));
        when(metricMapper.entityToMetricDTO(metricEntity)).thenReturn(metricDto);

        MetricDto result = metricSearchService.searchMetric(metricId);

        assertNotNull(result);
        assertEquals(metricDto, result);
        verify(metricRepository).findById(metricId);
        verify(metricMapper).entityToMetricDTO(metricEntity);
    }

    @Test
    void searchMetric_ShouldThrowException_WhenMetricDoesNotExist() {
        when(metricRepository.findById(metricId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            metricSearchService.searchMetric(metricId);
        });

        assertEquals("Metric not found", exception.getMessage());
        verify(metricRepository).findById(metricId);
    }

    @Test
    void findAllMetrics_ShouldReturnSetOfMetricDtos() {
        List<MetricEntity> metricEntities = new ArrayList<>(Arrays.asList(metricEntity));
        when(metricRepository.findAll()).thenReturn(metricEntities);
        when(metricMapper.entityToMetricDTO(metricEntity)).thenReturn(metricDto);

        Set<MetricDto> result = metricSearchService.findAllMetrics();

        assertEquals(1, result.size());
        assertTrue(result.contains(metricDto));
        verify(metricRepository).findAll();
        verify(metricMapper).entityToMetricDTO(metricEntity);
    }

    @Test
    void searchMetricsByFilter_ShouldReturnMetrics_WhenPredicatesAreValid() {
        List<MetricSearchDto> filterDtos = Arrays.asList(new MetricSearchDto("key", "eq", "value"));
        MetricEntity metricEntityFiltered = new MetricEntity(); // Настройте, если необходимо
        List<MetricEntity> metricEntities = new ArrayList<>(Collections.singletonList(metricEntityFiltered));

        when(metricRepository.findAll(any(Specification.class))).thenReturn(metricEntities);
        when(metricMapper.entityToMetricDTO(metricEntityFiltered)).thenReturn(metricDto);

        Set<MetricDto> result = metricSearchService.searchMetricsByFilter(filterDtos);

        assertEquals(1, result.size());
        assertTrue(result.contains(metricDto));
        verify(metricRepository).findAll(any(Specification.class));
        verify(metricMapper).entityToMetricDTO(metricEntityFiltered);
    }

    @Test
    void searchMetricsByFilter_ShouldThrowException_WhenFilterDtosAreEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            metricSearchService.searchMetricsByFilter(Collections.emptyList());
        });

        assertEquals("Predicates cannot be null", exception.getMessage());
    }


}
