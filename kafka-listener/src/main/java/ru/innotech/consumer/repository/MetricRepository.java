package ru.innotech.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.innotech.consumer.model.entity.MetricEntity;

import java.util.UUID;

public interface MetricRepository extends JpaRepository<MetricEntity, UUID>, JpaSpecificationExecutor<MetricEntity> {
}
