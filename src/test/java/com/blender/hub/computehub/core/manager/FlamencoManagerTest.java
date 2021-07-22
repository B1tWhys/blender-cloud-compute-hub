package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.manager.entity.FlamencoManager;
import com.blender.hub.computehub.core.manager.entity.ManagerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlamencoManagerTest extends AbstractManagerLinkingTest {
    FlamencoManager manager;

    @BeforeEach
    void setUp() {
        manager = FlamencoManager.builder()
                .id(MANAGER_ID)
                .build();
    }

    @Test
    public void testManagerType() {
        assertEquals(ManagerType.FLAMENCO, manager.getManagerType());
    }
}
