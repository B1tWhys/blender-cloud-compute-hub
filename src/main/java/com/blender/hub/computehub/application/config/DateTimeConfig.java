package com.blender.hub.computehub.application.config;

import com.blender.hub.computehub.adapter.util.TimeProviderImpl;
import com.blender.hub.computehub.core.util.TimeProvider;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeConfig {
    @Bean
    DateTimeFormatter dateTimeFormatter() {
        return ISODateTimeFormat.basicDateTime(); // FIXME
//        return new DateTimeFormatterBuilder()
//                .appendClockhourOfDay(1)
//                .appendLiteral(':')
//                .appendMinuteOfHour(1)
//                .appendLiteral(':')
//                .appendSecondOfMinute(1)
//                .appendLiteral('')
    }

    @Bean
    TimeProvider timeProvider() {
        return new TimeProviderImpl();
    }
}
