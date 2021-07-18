package com.blender.hub.computehub.core.manager.port.driving;

import com.blender.hub.computehub.core.manager.entity.Manager;

import java.util.List;

public interface ListManagers {
    List<Manager> mostRecentlyCreated(int limit);
}
