package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateManagerImpl implements CreateManager {
    private final ManagerIdGenerator idGenerator;
    private final ManagerRepo managerRepository;
    
    @Override
    public Manager createManager(CreateManagerCommand createManagerCommand) {
        Manager manager = Manager.builder()
                .id(idGenerator.generate())
                .build();
        managerRepository.upsert(manager);
        return manager;
    }
}
