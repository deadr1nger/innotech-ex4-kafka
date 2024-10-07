package ru.innotech.consumer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.innotech.consumer.model.dto.MetricDto;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_BYTES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.consumer.max-partition}")
    int maxPartitions;
    @Value("${spring.kafka.consumer.max-bytes}")
    int maxBytes;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(baseConfigs());
        props.put(MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitions);
        props.put(FETCH_MAX_BYTES_CONFIG, maxBytes);
        props.put(ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        return props;
    }


    private Map<String, Object> baseConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }


    public ConsumerFactory<String, MetricDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>(consumerConfigs());
        return new DefaultKafkaConsumerFactory<>(
                config, new StringDeserializer(),
                new JsonDeserializer<>(MetricDto.class));
    }

    @Bean
    KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>(baseConfigs());
        return new KafkaAdmin(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MetricDto> activationListener() {
        ConcurrentKafkaListenerContainerFactory<
                String, MetricDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
