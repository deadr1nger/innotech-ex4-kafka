package ru.innotech.consumer.model.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.entity.MetricEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MetricMapper {

    MetricDto entityToMetricDTO(MetricEntity message);
    MetricEntity metricDtoToEntity(MetricDto dto);
}
