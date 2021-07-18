package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CreateManagerImpl implements CreateManager {
    private final ManagerIdGenerator idGenerator;
    private final ManagerRepo managerRepository;
    private final ManagerInfraProxy infraProxy;
    
    @Override
    public Manager createManager(CreateManagerCommand createManagerCommand) {
        String id = idGenerator.generate();
        log.info("beginning manager creation with id: {}", id);
        Manager manager = Manager.builder()
                .id(id)
                .build();
        managerRepository.upsert(manager);
        Hostname managerHostname = infraProxy.createInfraFor(manager);
        manager.setHostname(managerHostname);
        managerRepository.upsert(manager);
        return manager;
    }
}
