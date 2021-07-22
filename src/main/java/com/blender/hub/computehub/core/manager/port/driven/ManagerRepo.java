package com.blender.hub.computehub.core.manager.port.driven;

import com.blender.hub.computehub.core.manager.entity.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerRepo {
    Optional<Manager> get(String id);
    Optional<Manager> getByHmacId(String hmacId);
    List<Manager> getMostRecentlyCreated(int limit);
    void upsert(Manager manager);
}
