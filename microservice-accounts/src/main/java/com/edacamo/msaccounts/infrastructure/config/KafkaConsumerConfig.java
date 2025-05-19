package com.edacamo.msaccounts.infrastructure.config;

import com.edacamo.msaccounts.domain.events.ClientDeletedEvent;
import com.edacamo.msaccounts.domain.events.ClientCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    // Consumer para ClientCreatedEvent
    @Bean
    public ConsumerFactory<String, ClientCreatedEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        // Configuración básica
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ms-accounts-client-created");

        // Usar ErrorHandlingDeserializer como wrapper
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Configurar los deserializadores internos
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // Configurar JsonDeserializer
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.edacamo.msaccounts.application.events");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientCreatedEvent.class.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // ← importante si tu Producer no usa headers

        log.info("Creating consumer factory******");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(ClientCreatedEvent.class, false))
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientCreatedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Puedes agregar un DefaultErrorHandler si quieres manejar errores específicos
        factory.setCommonErrorHandler(new DefaultErrorHandler());

        return factory;
    }

    // Consumer para ClientDeletedEvent
    @Bean
    public ConsumerFactory<String, ClientDeletedEvent> clientDeletedEventConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        // Configuración básica
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ms-accounts-client-deleted");

        // Usar ErrorHandlingDeserializer como wrapper
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Configurar los deserializadores internos
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // Configurar JsonDeserializer
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.edacamo.msaccounts.application.events");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClientDeletedEvent.class.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        log.info("Creating consumer factory for ClientDeletedEvent");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(ClientDeletedEvent.class, false))
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientDeletedEvent> clientDeletedEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientDeletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientDeletedEventConsumerFactory());
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;
    }
}
