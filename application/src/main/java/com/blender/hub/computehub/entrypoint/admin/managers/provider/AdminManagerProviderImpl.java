package com.blender.hub.computehub.entrypoint.admin.managers.provider;

import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.entrypoint.admin.managers.wire.AdminWireManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class AdminManagerProviderImpl implements AdminManagerProvider {
    ManagerRepo managerRepo;
    CreateManager createManagerUseCase;
    DateTimeFormatter dateTimeFormatter;

    @Override
    public List<AdminWireManager> listWireManagers(int limit) {
        return managerRepo.getMostRecentlyCreated(limit).stream().map(m -> AdminWireManager.builder()
                .id(m.getId())
                .state(m.getState().name())
                .humanReadableCreatedTs(new Instant(m.getCreatedTs()).toString(dateTimeFormatter))
                .build()).collect(Collectors.toList());
    }

    @Override
    public void create() {
        CreateManagerCommand command = CreateManagerCommand.builder().build();
        createManagerUseCase.createManager(command);
    }
}
