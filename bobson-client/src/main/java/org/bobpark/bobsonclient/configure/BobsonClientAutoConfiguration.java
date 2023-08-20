package org.bobpark.bobsonclient.configure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import org.bobpark.bobsonclient.configure.condition.BobsonClientCondition;
import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;
import org.bobpark.bobsonclient.event.client.impl.DefaultEventStoreApiClient;
import org.bobpark.bobsonclient.event.manager.EventManager;
import org.bobpark.bobsonclient.event.manager.EventManagerFactory;

@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(BobsonClientProperties.class)
@Conditional(BobsonClientCondition.class)
public class BobsonClientAutoConfiguration {

    private final BobsonClientProperties properties;
    private final ApplicationEventPublisher eventPublisher;

    @Bean
    public RestTemplate bobsonClientRestTemplate() {
        return new RestTemplateBuilder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    public BobSonApiClient bobSonApiClient() {
        return new DefaultEventStoreApiClient(properties, bobsonClientRestTemplate());
    }

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilder.class)
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        // Java time module
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addDeserializer(
            LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        Jackson2ObjectMapperBuilder builder =
            new Jackson2ObjectMapperBuilder() {
                @Override
                public void configure(@NonNull ObjectMapper objectMapper) {
                    super.configure(objectMapper);
                    objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
                    objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
                    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                }
            };
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modulesToInstall(jtm);

        return builder;
    }

    @Bean
    public EventManager eventCommandManager(ObjectMapper om) {
        return EventManagerFactory.builder()
            .objectMapper(om)
            .properties(properties)
            .apiClient(bobSonApiClient())
            .eventPublisher(eventPublisher)
            .build();
    }

}
