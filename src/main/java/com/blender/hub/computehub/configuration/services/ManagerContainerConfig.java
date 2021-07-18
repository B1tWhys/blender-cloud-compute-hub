package com.blender.hub.computehub.configuration.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("manager.container")
@Configuration
public class ManagerContainerConfig {
    private String imageName;
    private int apiPort;
}
