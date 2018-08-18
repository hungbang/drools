package com.bizlem.drools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
public class DroolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroolsApplication.class, args);

    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter
                = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }
}