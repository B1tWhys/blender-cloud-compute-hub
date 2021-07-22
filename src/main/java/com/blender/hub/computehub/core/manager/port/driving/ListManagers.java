package com.blender.hub.computehub.core.manager.port.driving;

import com.blender.hub.computehub.core.manager.entity.FlamencoManager;

import java.util.List;

public interface ListManagers {
    List<FlamencoManager> mostRecentlyCreated(int limit);
}
