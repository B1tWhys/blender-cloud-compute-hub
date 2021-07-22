package com.blender.hub.computehub.adapter.proxy.manager;

import com.blender.hub.computehub.configuration.services.ManagerDockerContainerProperties;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class LocalDockerManagerInfraProxyImplTest {
    private static final String IMAGE_NAME = "flamencoManager:1";
    private static final int FLAMENCO_PORT = 8000;
    private static final String CONTAINER_ID = "container-id";
    private static final int CONTAINER_PORT = 45678;

    @Mock
    DockerClient dockerClient;

    @Mock(answer = Answers.RETURNS_SELF)
    CreateContainerCmd createContainerCmd;

    @Mock(answer = Answers.RETURNS_SELF)
    InspectContainerCmd inspectContainerCmd;

    Manager manager;
    ManagerInfraProxy infraProxy;

    @BeforeEach
    void setUp() {
        manager = Manager.builder().id(UUID.randomUUID().toString()).build();

        // infraProxy setup
        ManagerDockerContainerProperties config = new ManagerDockerContainerProperties();
        config.setImageName(IMAGE_NAME);
        config.setApiPort(FLAMENCO_PORT);
        infraProxy = new LocalDockerManagerInfraProxyImpl(dockerClient, config);

        // dockerClient mock setup
        when(dockerClient.createContainerCmd(anyString()))
                .thenReturn(createContainerCmd);
        CreateContainerResponse createContainerResponse = new CreateContainerResponse();
        createContainerResponse.setId(CONTAINER_ID);
        when(createContainerCmd.exec()).thenReturn(createContainerResponse);
        when(dockerClient.startContainerCmd(CONTAINER_ID)).thenReturn(mock(StartContainerCmd.class));

        InspectContainerResponse inspectResponse = mock(InspectContainerResponse.class);
        NetworkSettings networkSettings = mock(NetworkSettings.class);
        when(dockerClient.inspectContainerCmd(CONTAINER_ID)).thenReturn(inspectContainerCmd);
        when(inspectContainerCmd.exec()).thenReturn(inspectResponse);
        when(inspectResponse.getNetworkSettings()).thenReturn(networkSettings);
        Ports ports = new Ports();
        ports.add(new PortBinding(Ports.Binding.bindPort(CONTAINER_PORT), ExposedPort.tcp(FLAMENCO_PORT)));
        when(networkSettings.getPorts()).thenReturn(ports);
    }

    @Test
    void containerIsCreated() {
        ArgumentCaptor<HostConfig> hostConfigArgCaptor = ArgumentCaptor.forClass(HostConfig.class);

        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).createContainerCmd(IMAGE_NAME);
        verify(createContainerCmd, times(1)).withName(manager.getId());
        verify(createContainerCmd, times(1)).withHostConfig(hostConfigArgCaptor.capture());
        verify(createContainerCmd, times(1)).withExposedPorts(ExposedPort.tcp(FLAMENCO_PORT));
        verify(createContainerCmd, times(1)).exec();

        Map<ExposedPort, Ports.Binding[]> bindings = hostConfigArgCaptor.getValue().getPortBindings().getBindings();
        String actualBinding = bindings.get(ExposedPort.tcp(FLAMENCO_PORT))[0].toString();
        assertEquals("0", actualBinding);
    }

    @Test
    void containerIsStarted() {
        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).startContainerCmd(CONTAINER_ID);
    }

    @Test
    void containerHostnameIsReturned() {
        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).inspectContainerCmd(CONTAINER_ID);

        Hostname returnedHostname = infraProxy.createInfraFor(manager);
        assertEquals(Hostname.builder()
                .scheme("http")
                .hostname("localhost")
                .port(CONTAINER_PORT)
                .build(), returnedHostname);
    }
}