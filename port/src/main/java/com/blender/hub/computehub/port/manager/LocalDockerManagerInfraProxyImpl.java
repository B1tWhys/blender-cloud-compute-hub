package com.blender.hub.computehub.port.manager;

import com.blender.hub.computehub.entity.manager.Hostname;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LocalDockerManagerInfraProxyImpl implements ManagerInfraProxy {
    private final DockerClient dockerClient;
    private final ManagerDockerContainerProperties config;

    @Override
    public Hostname createInfraFor(FlamencoManager manager) {
        String containerId = createManagerContainer(manager);
        log.info("Container id for manager {} is: {}", manager.getId(), containerId);

        int port = Integer.parseInt(getContainerPort(containerId));
        log.info("Manger: {} listening on host port: {}", manager.getId(), port);

        return Hostname.builder()
                .scheme(config.getManagerUrlScheme())
                .hostname("localhost")
                .port(port)
                .build();
    }

    private String createManagerContainer(FlamencoManager manager) {
        log.info("Creating docker container for manager id: {}", manager);
        PortBinding portBinding = PortBinding.parse("0:" + config.getApiPort() + "/tcp");
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(config.getImageName())
                .withName(manager.getId())
                .withHostConfig(new HostConfig().withPortBindings(portBinding))
                .withExposedPorts(ExposedPort.tcp(config.getApiPort()));
        log.debug("Create container command: {}", createContainerCmd);
        CreateContainerResponse createResponse = createContainerCmd.exec();

        String containerid = createResponse.getId();
        dockerClient.startContainerCmd(containerid).exec();
        return containerid;
    }

    private String getContainerPort(String containerId) {
        NetworkSettings networkSettings = dockerClient.inspectContainerCmd(containerId).exec().getNetworkSettings();
        log.debug("network settings for container {}: {}", containerId, networkSettings);
        Ports ports = networkSettings.getPorts();
        Ports.Binding[] bindings = ports.getBindings().get(ExposedPort.tcp(config.getApiPort()));
        return bindings[0].getHostPortSpec();
    }
}
