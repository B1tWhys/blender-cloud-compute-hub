package com.blender.hub.computehub.core.mock;


import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryManagerRepository implements ManagerRepo {
    public Map<String, Manager> managerMap = new HashMap<>();
    
    @Override
    public Optional<Manager> get(String id) {
        return Optional.ofNullable(managerMap.get(id))
                .map(m -> m.toBuilder().build());
    }

    @Override
    public Optional<Manager> getByHmacId(String hmacId) {
        return Optional.empty(); // FIXME
    }

    @Override
    public List<Manager> getMostRecentlyCreated(int limit) {
        return null; // FIXME
    }

    @Override
    public void upsert(Manager manager) {
        managerMap.put(manager.getId(), manager.toBuilder().build());
    }
}
