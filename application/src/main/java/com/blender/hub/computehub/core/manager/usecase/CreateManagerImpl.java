package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.FlamencoManager;
import com.blender.hub.computehub.core.manager.entity.ManagerState;
import com.blender.hub.computehub.core.manager.port.driven.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.driven.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import com.blender.hub.computehub.core.util.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CreateManagerImpl implements CreateManager {
    private final ManagerIdGenerator idGenerator;
    private final ManagerRepo managerRepository;
    private final ManagerInfraProxy infraProxy;
    private final LinkManager linkManager;
    private final TimeProvider timeProvider;

    @Override
    public FlamencoManager createManager(CreateManagerCommand createManagerCommand) {
        String id = idGenerator.generate();
        log.info("beginning manager creation with id: {}", id);
        FlamencoManager manager = FlamencoManager.builder()
                .id(id)
                .createdTs(timeProvider.now())
                .state(ManagerState.NEW)
                .build();
        managerRepository.upsert(manager);
        Hostname managerHostname = infraProxy.createInfraFor(manager);
        manager.setHostname(managerHostname);
        managerRepository.upsert(manager);
        linkManager.link(manager);
        return manager;
    }
}
