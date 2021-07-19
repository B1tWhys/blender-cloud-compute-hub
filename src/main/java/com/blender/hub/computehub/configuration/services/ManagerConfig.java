package com.blender.hub.computehub.configuration.services;

import com.blender.hub.computehub.adapter.persistance.InMemoryManagerRepoImpl;
import com.blender.hub.computehub.adapter.proxy.manager.LocalDockerManagerInfraProxyImpl;
import com.blender.hub.computehub.adapter.proxy.manager.ManagerIdGeneratorImpl;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProviderImpl;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.MockAdminManagerProviderImpl;
import com.github.dockerjava.api.DockerClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerConfig {
    @Bean(name = "adminManagerProvider")
    @ConditionalOnProperty(value = "mock.adminManagerProvider", havingValue = "true")
    AdminManagerProvider mockAdminManagerProvider() {
        return new MockAdminManagerProviderImpl();
    }

    @Bean(name = "adminManagerProvider")
    @ConditionalOnProperty(value = "mock.adminManagerProvider", havingValue = "false", matchIfMissing = true)
    AdminManagerProvider realAdminManagerProvider(ManagerRepo managerRepo, CreateManager createManagerUseCase) {
        return new AdminManagerProviderImpl(managerRepo, createManagerUseCase);
    }

    @Bean
    @ConditionalOnMissingBean(type = "ManagerRepo")
    public ManagerRepo inMemoryManagerRepoImpl() {
        return new InMemoryManagerRepoImpl();
    }

    @Bean
    public CreateManager createManagerUseCase(ManagerRepo managerRepo, ManagerInfraProxy managerInfraProxy) {
        return new CreateManagerImpl(managerIdGenerator(), managerRepo, managerInfraProxy);
    }

    @Bean
    public ManagerIdGenerator managerIdGenerator() {
        return new ManagerIdGeneratorImpl();
    }

    @Bean(name = "managerInfraProxy")
    @ConditionalOnProperty(value = "infrastructure.type", havingValue = "local-docker", matchIfMissing = true)
    public ManagerInfraProxy localDockerManagerInfraProxy(DockerClient dockerClient,
                                                          ManagerDockerContainerProperties managerDockerContainerProperties) {
        return new LocalDockerManagerInfraProxyImpl(dockerClient, managerDockerContainerProperties);
    }
}
