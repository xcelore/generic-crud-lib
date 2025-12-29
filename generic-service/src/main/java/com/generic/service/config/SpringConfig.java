package com.generic.service.config;

import com.generic.service.service.GenericTimeCreator;
import com.generic.service.service.IdGenerationStrategy;
import com.generic.service.service.impl.DefaultIdGenerationStrategy;
import com.generic.service.service.impl.DefaultTimeCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    @ConditionalOnMissingBean(IdGenerationStrategy.class)
    public IdGenerationStrategy idGenerationStrategy() {
        return new DefaultIdGenerationStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(GenericTimeCreator.class)
    public GenericTimeCreator genericTimeCreator() {
        return new DefaultTimeCreator();
    }

}
