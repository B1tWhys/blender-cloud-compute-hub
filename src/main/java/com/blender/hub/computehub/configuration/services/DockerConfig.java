package com.blender.hub.computehub.configuration.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@ConditionalOnProperty(value = "infrastructure.type", havingValue = "local-docker", matchIfMissing = true)
public class DockerConfig {
    @Bean
    DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
    }

    @Bean
    DockerHttpClient dockerHttpClient() {
        return new ZerodepDockerHttpClient.Builder()
                .dockerHost(UriComponentsBuilder.fromUriString("unix:///var/run/docker.sock").build().toUri())
                .build();
    }

    @Bean
    DockerClient dockerClient() {
        return DockerClientImpl.getInstance(dockerClientConfig(), dockerHttpClient());
    }
}
