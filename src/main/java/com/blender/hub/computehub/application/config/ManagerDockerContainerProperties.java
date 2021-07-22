package com.blender.hub.computehub.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("manager.container")
@Configuration
public class ManagerDockerContainerProperties {
    private String imageName;
    private int apiPort;
    private String managerUrlScheme = "http";
}
