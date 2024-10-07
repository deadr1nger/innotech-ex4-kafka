package ru.innotech.consumer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Entity
@Table(name = "metrics")
@Getter
@Setter
public class MetricEntity {
    @Id
    UUID id;
    @Column(name = "system_name")
    String systemName;
    LocalDateTime date;
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Double> metrics;
}
