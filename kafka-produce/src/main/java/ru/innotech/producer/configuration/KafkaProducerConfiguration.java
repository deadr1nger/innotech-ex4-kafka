package ru.innotech.producer.configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.innotech.producer.model.dto.MetricDto;

import java.util.HashMap;
import java.util.Map;


import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
@Slf4j
@Configuration
@EnableKafka
public class KafkaProducerConfiguration {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(baseConfigs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        return props;
    }

    private Map<String, Object> baseConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    private ProducerFactory<String, MetricDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }



    @Bean
    public KafkaTemplate<String, MetricDto> kafkaTemplate() {
        KafkaTemplate<String, MetricDto> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(producerListener());
        return kafkaTemplate;
    }


    @Bean
    KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>(baseConfigs());
        return new KafkaAdmin(configs);
    }


    @Bean
    public ProducerListener<String, MetricDto> producerListener() {
        return new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, MetricDto> producerRecord, RecordMetadata recordMetadata) {
                log.info(String.format("Message sent successfully with offset: %s", recordMetadata.offset()));
            }

            @Override
            public void onError(ProducerRecord<String, MetricDto> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.info(String.format("Error sending message: %s ", exception.getMessage()));
            }
        };
    }
}
