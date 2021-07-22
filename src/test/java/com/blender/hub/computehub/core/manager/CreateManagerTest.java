package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.entity.ManagerState;
import com.blender.hub.computehub.core.manager.port.driven.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.mock.InMemoryManagerRepository;
import com.blender.hub.computehub.core.mock.MockManagerIdGenerator;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CreateManagerTest {
    private static final String MANAGER_HOSTNAME = "testHostname";

    protected MockManagerIdGenerator managerIdGenerator;
    protected InMemoryManagerRepository managerRepository;

    @Mock
    protected ManagerInfraProxy managerInfraProxy;

    @Mock
    protected LinkManager linkManager;

    protected Manager manager;
    CreateManager createManager;
    
    @BeforeEach
    void setUp() {
        manager = null;
        managerIdGenerator = new MockManagerIdGenerator();
        managerRepository = new InMemoryManagerRepository();

        Mockito.when(managerInfraProxy.createInfraFor(Mockito.any(Manager.class)))
                .thenReturn(Hostname.builder().scheme("http").hostname(MANAGER_HOSTNAME).port(1234).build());

        createManager = new CreateManagerImpl(managerIdGenerator,
                managerRepository,
                managerInfraProxy,
                linkManager);
    }
    
    @Test
    void testCreateManager() {
        createManager();
        assertNotNull(manager);
    }
    
    @Test
    void testCreateManagerWithGeneratedId() {
        createManager();
        String lastManagerId = String.valueOf(managerIdGenerator.lastManagerId);
        assertEquals(lastManagerId, manager.getId());
    }

    @Test
    void testCreateManagerWithCurrentDate() {
        createManager();
        assertNotNull(manager.getCreatedTs());
        Period diff = new Period(new DateTime(), manager.getCreatedTs(), PeriodType.millis());
        assertEquals(diff.getMillis(), 0, 30);
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
        assertNotNull(managerRepository.get(manager.getId()));
    }

    @Test
    void testManagerInfraCreated() {
        createManager();
        Mockito.verify(managerInfraProxy, Mockito.times(1))
                .createInfraFor(Mockito.any(Manager.class));
        assertEquals(MANAGER_HOSTNAME, manager.getHostname().getHostname(), "manager hostname must match infrastructure");
    }

    @Test
    void managerIsLinked() {
        createManager();
        Mockito.verify(linkManager, Mockito.times(1))
                .link(Mockito.eq(manager));
    }

    // TODO: tests & implementation for manager state changes

    private void createManager() {
        CreateManagerCommand command = CreateManagerCommand.builder().build();
        manager = createManager.createManager(command);
    }
    
}
