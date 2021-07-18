package com.blender.hub.computehub.configuration.services;

import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.MockAdminManagerProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderBeanConfiguration {
    @Bean(name = "adminManagerProvider")
    AdminManagerProvider mockAdminManagerProvider() {
        return new MockAdminManagerProviderImpl();
    }
}
