package com.edacamo.mspersons.infrastructure.messaging;

import com.edacamo.mspersons.domain.events.ClientDeletedEvent;
import com.edacamo.mspersons.domain.events.ClientEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final String bootstrapAddress = "kafka:9092";

    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Configuración para garantizar persistencia y reintentos
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // KafkaTemplate para ClientCreatedEvent
    @Bean
    public KafkaTemplate<String, ClientEvent> clientEventKafkaTemplate(ProducerFactory<String, ClientEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // KafkaTemplate para ClientDeletedEvent
    @Bean
    public KafkaTemplate<String, ClientDeletedEvent> clientDeletedEventKafkaTemplate(ProducerFactory<String, ClientDeletedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
