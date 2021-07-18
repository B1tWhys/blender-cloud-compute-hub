package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.mock.InMemoryManagerRepository;
import com.blender.hub.computehub.core.mock.MockManagerIdGenerator;
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

    protected Manager manager;
    CreateManager createManager;
    
    @BeforeEach
    void setUp() {
        manager = null;
        managerIdGenerator = new MockManagerIdGenerator();
        managerRepository = new InMemoryManagerRepository();

        Mockito.when(managerInfraProxy.createInfraFor(Mockito.any(Manager.class)))
                .thenReturn(new Hostname(MANAGER_HOSTNAME));

        createManager = new CreateManagerImpl(managerIdGenerator,
                managerRepository,
                managerInfraProxy);
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
    
    private void createManager() {
        CreateManagerCommand command = CreateManagerCommand.builder().build();
        manager = createManager.createManager(command);
    }
    
}
