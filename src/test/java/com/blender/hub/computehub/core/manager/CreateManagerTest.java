package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.mock.InMemoryManagerRepository;
import com.blender.hub.computehub.core.mock.MockManagerIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateManagerTest {
    protected MockManagerIdGenerator managerIdGenerator;
    protected InMemoryManagerRepository managerRepository;
    
    protected Manager manager;
    CreateManager createManager;
    
    @BeforeEach
    void setUp() {
        manager = null;
        managerIdGenerator = new MockManagerIdGenerator();
        managerRepository = new InMemoryManagerRepository();
        createManager = new CreateManagerImpl(managerIdGenerator,
                                          managerRepository);
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
        assertEquals(lastManagerId, manager.id);
    }
    
    @Test
    void testManagerPersisted() {
        createManager();
        assertNotNull(managerRepository.get(manager.id));
    }
    
    private void createManager() {
        CreateManagerCommand command = CreateManagerCommand.builder().build();
        manager = createManager.createManager(command);
    }
    
}
