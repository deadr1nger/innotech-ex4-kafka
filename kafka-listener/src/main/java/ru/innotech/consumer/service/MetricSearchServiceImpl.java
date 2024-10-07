package ru.innotech.consumer.service;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
public class MetricSearchServiceImpl<T> implements MetricSearchService{
    private final MetricRepository metricRepository;
    private final MetricMapper metricMapper;
    @Override
    public MetricDto searchMetric(UUID id) {
        return metricRepository.findById(id).map(metricMapper::entityToMetricDTO).orElseThrow(() -> new IllegalArgumentException("Metric not found"));
    }

    @Override
    public Set<MetricDto> findAllMetrics() {
        return metricRepository.findAll().stream().map(metricMapper::entityToMetricDTO).collect(Collectors.toSet());
    }

    @Override
    public Set<MetricDto> searchMetricsByFilter(List<MetricSearchDto> dtos) {
        if (dtos.isEmpty()) {
            throw new IllegalArgumentException("Predicates cannot be null");
        }
        Specification<MetricEntity> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            dtos.stream().forEach(filterDto -> {
                if (filterDto.getOperator().equals("eq"))
                    predicates.add(builder.equal(setPath((Root<T>) root, filterDto.getKey()), filterDto.getValue()));
                if (filterDto.getOperator().equals("uuid"))
                    predicates.add(builder.equal(setPath((Root<T>) root, filterDto.getKey()), UUID.fromString(filterDto.getValue())));
                if (filterDto.getOperator().equals("gt"))
                    predicates.add(builder.gt(setPath((Root<T>) root, filterDto.getKey()), Double.parseDouble(filterDto.getValue())));
                if (filterDto.getOperator().equals("lt"))
                    predicates.add(builder.lt(setPath((Root<T>) root, filterDto.getKey()), Double.parseDouble(filterDto.getValue())));

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


    private Path setPath(Root<T> root, String key) {
        String[] path = key.split(":");
        Path currentPath = root;

        for (String pathElement : path) {
            currentPath = currentPath.get(pathElement);
        }

        return currentPath;
    }
}
