package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Manager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class CreateManagerTest extends AbstractManagerLinkingTest {
    @Captor
    ArgumentCaptor<Manager> managerArgumentCaptor;

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
//
//    @Test
//    void testCreateManagerWithState() {
//        createManager();
//        assertNotNull(manager.getState());
//        assertEquals(manager.getState(), ManagerState.NEW);
//    }
//
//    @Test
//    void testManagerPersisted() {
//        createManager();
//        assertNotNull(managerRepository.get(manager.getId()));
//    }
//
//    @Test
//    void testManagerInfraCreated() {
//        createManager();
//        Mockito.verify(managerInfraProxy, Mockito.times(1))
//                .createInfraFor(Mockito.any(Manager.class));
//        assertEquals(MANAGER_HOSTNAME, manager.getHostname().getHostname(), "manager hostname must match infrastructure");
//    }
//
//    @Test
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
