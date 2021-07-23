package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.FlamencoManager;
import com.blender.hub.computehub.core.manager.entity.ManagerState;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.manager.usecase.LinkManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateFlamencoManagerTest extends AbstractManagerLinkingTest {
    @Captor
    ArgumentCaptor<FlamencoManager> managerArgumentCaptor;

    @BeforeEach
    void setUp() {
        setupMockAdapters();
        initUseCase();
    }

    protected void setupMockAdapters() {
        when(hmacIdGenerator.generate()).thenReturn(SECRET_ID);
        when(timeProvider.now()).thenReturn(NOW_TS);
        when(managerInfraProxy.createInfraFor(any(FlamencoManager.class)))
                .thenReturn(Hostname.builder().scheme("http").hostname(MANAGER_HOSTNAME).port(1234).build());
        when(managerIdGenerator.generate()).thenReturn(MANAGER_ID);
        when(proxyFactory.buildManagerProxy(any(FlamencoManager.class))).thenReturn(managerProxy);
        when(managerProxy.exchangeHmacSecret()).thenAnswer(i ->
                createHmacSecret.newLinkTimeHmacSecret(SECRET_VALUE).getId());
        when(hmacSecretRepository.getHmacSecret(SECRET_ID)).thenReturn(HmacSecret.builder()
                .id(SECRET_ID)
                .value(SECRET_VALUE).build());
    }

    protected void initUseCase() {
        linkManager = new LinkManagerImpl(proxyFactory, managerRepository, hmacSecretRepository);
        createHmacSecret = new CreateHmacSecretImpl(hmacIdGenerator, hmacSecretRepository);
        createManager = new CreateManagerImpl(managerIdGenerator, managerRepository, managerInfraProxy, linkManager, timeProvider);
    }

    @Test
    void testCreateManager() {
        createManager();
        assertNotNull(manager);
        assertEquals(manager.getId(), managerArgumentCaptor.getValue().getId());
    }

    @Test
    void testCreateManagerWithGeneratedId() {
        createManager();

        assertEquals(MANAGER_ID, manager.getId());
    }

    @Test
    void testCreateManagerWithCurrentDate() {
        createManager();

        assertEquals(NOW_TS, manager.getCreatedTs());
    }

    @Test
    void testCreateManagerWithState() {
        createManager();
        assertNotNull(manager.getState());
        assertEquals(manager.getState(), ManagerState.NEW);
    }

    @Test
    void testManagerPersisted() {
        createManager();
        assertEquals(managerArgumentCaptor.getValue(), manager);
    }

    @Test
    void testManagerInfraCreated() {
        createManager();
        Mockito.verify(managerInfraProxy, Mockito.times(1))
                .createInfraFor(Mockito.any(FlamencoManager.class));
        assertEquals(MANAGER_HOSTNAME, manager.getHostname().getHostname(), "manager hostname must match infrastructure");
    }

//    @Test // TODO: redo this when linking is triggered by event
//    void managerIsLinked() {
//        createManager();
//        Mockito.verify(linkManager, Mockito.times(1))
//                .link(Mockito.eq(manager));
//    }
//
//    // TODO: tests & implementation for manager state changes
//
    private void createManager() {
        CreateManagerCommand command = CreateManagerCommand.builder().build();
        manager = createManager.createManager(command);
        verify(managerRepository, Mockito.atLeastOnce()).upsert(managerArgumentCaptor.capture());
    }
}
