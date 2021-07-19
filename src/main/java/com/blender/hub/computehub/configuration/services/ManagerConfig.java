package com.blender.hub.computehub.configuration.services;

import com.blender.hub.computehub.adapter.persistance.InMemoryManagerRepoImpl;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.MockAdminManagerProviderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerConfig {
    @Bean(name = "adminManagerProvider")
    @ConditionalOnMissingBean(type = "AdminManagerProvider")
    AdminManagerProvider mockAdminManagerProvider() {
        return new MockAdminManagerProviderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(type = "ManagerRepo")
    public ManagerRepo inMemoryManagerRepoImpl() {
        return new InMemoryManagerRepoImpl();
    }
}
