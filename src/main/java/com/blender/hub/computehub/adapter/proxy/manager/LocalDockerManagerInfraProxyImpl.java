package com.blender.hub.computehub.adapter.proxy.manager;

import com.blender.hub.computehub.configuration.services.ManagerDockerContainerProperties;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Ports;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LocalDockerManagerInfraProxyImpl implements ManagerInfraProxy {
    private final DockerClient dockerClient;
    private final ManagerDockerContainerProperties config;

    @Override
    public Hostname createInfraFor(Manager manager) {
        log.info("Creating docker container for manager id: {}", manager);
        CreateContainerResponse createResponse = dockerClient.createContainerCmd(config.getImageName())
                .withName(manager.getId())
                .withExposedPorts(ExposedPort.tcp(config.getApiPort()))
                .exec();

        NetworkSettings networkSettings = dockerClient.inspectContainerCmd(createResponse.getId()).exec().getNetworkSettings();
        Ports ports = networkSettings.getPorts();
        Ports.Binding[] bindings = ports.getBindings().get(ExposedPort.tcp(config.getApiPort()));
        String port = bindings[0].getHostPortSpec();

        return new Hostname("localhost:" + port);
    }
}
