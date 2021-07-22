package com.blender.hub.computehub.application.services;

import com.blender.hub.computehub.adapter.util.TimeProviderImpl;
import com.blender.hub.computehub.core.util.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeProviderConfig {
    @Bean
    TimeProvider timeProvider() {
        return new TimeProviderImpl();
    }
}
