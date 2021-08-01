package com.blender.hub.computehub.port.manager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("manager.container")
@Configuration
public class ManagerDockerContainerProperties { // FIXME: I don't think this really belongs in this module...
    private String imageName;
    private int apiPort;
    private String managerUrlScheme = "http";
}
