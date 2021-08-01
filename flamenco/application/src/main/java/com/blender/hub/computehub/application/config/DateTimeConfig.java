package com.blender.hub.computehub.application.config;

import com.blender.hub.computehub.port.util.TimeProviderImpl;
import com.blender.hub.computehub.usecase.util.TimeProvider;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeConfig {
    @Bean
    DateTimeFormatter dateTimeFormatter() {
        return ISODateTimeFormat.basicDateTime(); // FIXME
    }

    @Bean
    TimeProvider timeProvider() {
        return new TimeProviderImpl();
    }
}
