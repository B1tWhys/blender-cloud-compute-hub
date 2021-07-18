package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.ListManagers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class ListManagersImpl implements ListManagers {
    ManagerRepo managerRepo;

    @Override
    public List<Manager> mostRecentlyCreated(int limit) {
        return null;
    }
}
