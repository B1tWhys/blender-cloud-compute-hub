package com.blender.hub.computehub.core.manager.port.driven;

import com.blender.hub.computehub.core.manager.entity.Manager;

import java.util.Optional;

public interface GetManager {
    Optional<Manager> get(String id);
}
