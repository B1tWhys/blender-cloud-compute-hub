package com.blender.hub.computehub.usecase.manager.port.driving;

import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;

import java.util.List;

public interface ListManagers {
    List<FlamencoManager> mostRecentlyCreated(int limit);
}
