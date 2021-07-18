package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.configuration.services.ManagerContainerConfig;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import org.apache.catalina.Host;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalDockerManagerInfraProxyImplTest {
    public static final String IMAGE_NAME = "flamencoManager:1";
    public static final int FLAMENCO_PORT = 8000;
    @Mock
    DockerClient dockerClient;

    @Mock(answer = Answers.RETURNS_SELF)
    CreateContainerCmd createContainerCmd;

    Manager manager;
    ManagerInfraProxy infraProxy;

    @BeforeEach
    void setUp() {
        manager = Manager.builder().id(UUID.randomUUID().toString()).build();

        // infraProxy setup
        ManagerContainerConfig config = new ManagerContainerConfig();
        config.setImageName(IMAGE_NAME);
        config.setApiPort(FLAMENCO_PORT);
        infraProxy = new LocalDockerManagerInfraProxyImpl(dockerClient, config);

        // dockerClient mock setup
        when(dockerClient.createContainerCmd(anyString()))
                .thenReturn(createContainerCmd);
    }

    @Test
    void containerIsCreated() {
        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).createContainerCmd(IMAGE_NAME);
        verify(createContainerCmd, times(1)).withName(manager.getId());
        verify(createContainerCmd, times(1)).withExposedPorts(ExposedPort.tcp(FLAMENCO_PORT));
        verify(createContainerCmd, times(1)).exec();
    }
}