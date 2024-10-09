package ru.innotech.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.entity.MetricEntity;
import ru.innotech.consumer.model.mapper.MetricMapper;
import ru.innotech.consumer.repository.MetricRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class MetricServiceImplTest {

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MetricMapper metricMapper;

    @InjectMocks
    private MetricServiceImpl metricService;

    private MetricDto metricDto;
    private MetricEntity metricEntity;
    private String validMessage;

    @BeforeEach
    void setUp() {
        metricDto = new MetricDto();
        metricEntity = new MetricEntity();
        validMessage = "{\"key\":\"value\"}";
    }

    @Test
    void saveMetric_ShouldSaveMetric_WhenMessageIsValid() throws JsonProcessingException {
        when(objectMapper.readValue(validMessage, MetricDto.class)).thenReturn(metricDto);
        when(metricMapper.metricDtoToEntity(metricDto)).thenReturn(metricEntity);

        metricService.saveMetric(validMessage);

        ArgumentCaptor<MetricEntity> captor = ArgumentCaptor.forClass(MetricEntity.class);
        verify(metricRepository).save(captor.capture());
        assertEquals(metricEntity, captor.getValue());
    }

    @Test
    void saveMetric_ShouldLogError_WhenJsonProcessingFails() throws JsonProcessingException {
        when(objectMapper.readValue(validMessage, MetricDto.class)).thenThrow(JsonProcessingException.class);
        metricService.saveMetric(validMessage);
        verify(metricRepository, never()).save(any());

    }

    @Test
    void saveMetric_ShouldLogError_WhenExceptionOccurs() throws JsonProcessingException {
        when(objectMapper.readValue(validMessage, MetricDto.class)).thenThrow(new JsonProcessingException("Error") {});
        metricService.saveMetric(validMessage);
        verify(metricRepository, never()).save(any());
    }
}
