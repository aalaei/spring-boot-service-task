package com.swisscom.tasks.task3.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Bean for {@link ModelMapper}.
 */
@Component
public class DTOMapperBean {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper().registerModule(new RecordModule());
    }
}
