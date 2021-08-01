package com.blender.hub.computehub.usecase.manager;

import com.blender.hub.computehub.entity.manager.ManagerType;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(ManagerType.FLAMENCO, manager.getManagerType());
    }
}
