package org.bobpark.bobsonclient.configure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.client.RestTemplate;

import org.bobpark.bobsonclient.configure.condition.BobsonClientCondition;
import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;

@AutoConfiguration
@EnableConfigurationProperties(BobsonClientProperties.class)
@Conditional(BobsonClientCondition.class)
public class BobsonClientAutoConfiguration {

    @Bean
    public RestTemplate bobsonClientRestTemplate() {
        return new RestTemplate();
    }
}
