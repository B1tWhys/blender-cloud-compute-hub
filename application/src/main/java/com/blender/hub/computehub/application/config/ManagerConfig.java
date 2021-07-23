package com.blender.hub.computehub.application.config;

import com.blender.hub.computehub.port.hmac.HmacSecretIdGeneratorImpl;
import com.blender.hub.computehub.port.manager.LocalDockerManagerInfraProxyImpl;
import com.blender.hub.computehub.port.manager.ManagerDockerContainerProperties;
import com.blender.hub.computehub.port.manager.ManagerIdGeneratorImpl;
import com.blender.hub.computehub.port.manager.ManagerProxyFactoryImpl;
import com.blender.hub.computehub.port.persistance.InMemoryManagerRepoImpl;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.usecase.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerIdGenerator;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerInfraProxy;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxyFactory;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.usecase.manager.port.driving.CreateManager;
import com.blender.hub.computehub.usecase.manager.port.driving.LinkManager;
import com.blender.hub.computehub.usecase.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.usecase.manager.usecase.LinkManagerImpl;
import com.blender.hub.computehub.usecase.util.TimeProvider;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProviderImpl;
import com.blender.hub.computehub.entrypoint.admin.managers.provider.MockAdminManagerProviderImpl;
import com.github.dockerjava.api.DockerClient;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ManagerConfig {
    @Bean(name = "adminManagerProvider")
    @ConditionalOnProperty(value = "mock.adminManagerProvider", havingValue = "true")
    AdminManagerProvider mockAdminManagerProvider() {
        return new MockAdminManagerProviderImpl();
    }

    @Bean(name = "adminManagerProvider")
    @ConditionalOnProperty(value = "mock.adminManagerProvider", havingValue = "false", matchIfMissing = true)
    AdminManagerProvider realAdminManagerProvider(ManagerRepo managerRepo,
                                                  CreateManager createManagerUseCase,
                                                  DateTimeFormatter dateTimeFormatter) {
        return new AdminManagerProviderImpl(managerRepo, createManagerUseCase, dateTimeFormatter);
    }

    @Bean
    @ConditionalOnMissingBean(type = "ManagerRepo")
    public ManagerRepo inMemoryManagerRepoImpl() {
        return new InMemoryManagerRepoImpl();
    }

    @Bean
    public CreateManager createManagerUseCase(ManagerRepo managerRepo,
                                              ManagerInfraProxy managerInfraProxy,
                                              LinkManager linkManager,
                                              TimeProvider timeProvider) {
        return new CreateManagerImpl(managerIdGenerator(), managerRepo, managerInfraProxy, linkManager, timeProvider);
    }

    @Bean
    public LinkManager linkManagerUseCase(ManagerProxyFactory proxyFactory,
                                          ManagerRepo managerRepo,
                                          HmacSecretRepository hmacSecretRepository) {
        return new LinkManagerImpl(proxyFactory, managerRepo, hmacSecretRepository);
    }

    @Bean
    public ManagerProxyFactory managerProxyFactory(RestTemplate noRedirectRestTemplate) {
        return new ManagerProxyFactoryImpl(noRedirectRestTemplate);
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

    @Bean
    public CreateHmacSecretImpl createHmacSecret(HmacSecretRepository hmacSecretRepository) {
        return new CreateHmacSecretImpl(secretIdGenerator(), hmacSecretRepository);
    }

    @Bean
    public HmacSecretIdGenerator secretIdGenerator() {
        return new HmacSecretIdGeneratorImpl();
    }
}
