package ru.innotech.consumer.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.innotech.consumer.model.dto.MetricDto;
import ru.innotech.consumer.model.dto.MetricSearchDto;
import ru.innotech.consumer.model.entity.MetricEntity;
import ru.innotech.consumer.model.mapper.MetricMapper;
import ru.innotech.consumer.repository.MetricRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricSearchServiceImpl implements MetricSearchService {
    private final MetricRepository metricRepository;
    private final MetricMapper metricMapper;

    @Override
    @Transactional(readOnly = true)
    public MetricDto searchMetric(UUID id) {
        return metricRepository.findById(id).map(metricMapper::entityToMetricDTO).orElseThrow(() -> new IllegalArgumentException("Metric not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<MetricDto> findAllMetrics() {
        return metricRepository.findAll().stream().map(metricMapper::entityToMetricDTO).collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<MetricDto> searchMetricsByFilter(List<MetricSearchDto> dtos) {
        if (dtos.isEmpty()) {
            throw new IllegalArgumentException("Predicates cannot be null");
        }
        Specification<MetricEntity> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            dtos.forEach(filterDto -> {
                switch (filterDto.getOperator()) {
                    case "eq":
                        predicates.add(builder.equal(root.get(filterDto.getKey()), filterDto.getValue()));
                        break;
                    case "uuid":
                        predicates.add(builder.equal(root.get(filterDto.getKey()), UUID.fromString(filterDto.getValue())));
                        break;
                    case "gt":
                        predicates.add(builder.gt(root.get(filterDto.getKey()), Double.parseDouble(filterDto.getValue())));
                        break;
                    case "lt":
                        predicates.add(builder.lt(root.get(filterDto.getKey()), Double.parseDouble(filterDto.getValue())));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + filterDto.getOperator());
                }
            });
            if (predicates.isEmpty()) {
                throw new IllegalArgumentException("Predicates not found");
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };

        return metricRepository.findAll(spec)
                .stream()
                .map(metricMapper::entityToMetricDTO).collect(Collectors.toSet());
    }
}
