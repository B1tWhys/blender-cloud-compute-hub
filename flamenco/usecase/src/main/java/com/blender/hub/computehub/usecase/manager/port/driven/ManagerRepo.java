package com.blender.hub.computehub.usecase.manager.port.driven;

import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;

import java.util.List;
import java.util.Optional;

public interface ManagerRepo {
    Optional<FlamencoManager> get(String id);
    Optional<FlamencoManager> getByHmacId(String hmacId);
    List<FlamencoManager> getMostRecentlyCreated(int limit);
    void upsert(FlamencoManager manager);
}
