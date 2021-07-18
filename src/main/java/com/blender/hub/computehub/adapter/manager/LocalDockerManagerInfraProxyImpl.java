package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.configuration.services.ManagerContainerConfig;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LocalDockerManagerInfraProxyImpl implements ManagerInfraProxy {
    private final DockerClient dockerClient;
    private final ManagerContainerConfig config;

    @Override
    public Hostname createInfraFor(Manager manager) {
        log.info("Creating docker container for manager id: {}", manager);
        dockerClient.createContainerCmd(config.getImageName())
                .withName(manager.getId())
                .withExposedPorts(ExposedPort.tcp(config.getApiPort()))
                .exec();
        return null;
    }
}
