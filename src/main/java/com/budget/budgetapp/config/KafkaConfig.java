package com.budget.budgetapp.config;

import com.budget.budgetapp.kafka.Event;
import com.budget.budgetapp.kafka.UserEvent;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {

        Map<String, Object> config = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "com.budget.budgetapp.kafka"
        );

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(UserEvent.class));
    }

//    @Bean
//    public RecordMessageConverter messageConverter() {
//        StringJsonMessageConverter converter = new StringJsonMessageConverter();
//        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//        typeMapper.addTrustedPackages("com.budget.budgetapp.kafka");
//        HashMap<String, Class<?>> mappings = new HashMap<>();
//        mappings.put("userEvent", UserEvent.class);
//        typeMapper.setIdClassMapping(mappings);
//        converter.setTypeMapper(typeMapper);
//
//        return converter;
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent>
    kafkaListenerContainerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(bootstrapServers));
//        factory.setRecordMessageConverter(messageConverter());
        return factory;
    }
}
