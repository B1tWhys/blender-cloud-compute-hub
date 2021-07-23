package com.blender.hub.computehub.core.manager.port.driving;


import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.FlamencoManager;

public interface CreateManager {
    FlamencoManager createManager(CreateManagerCommand createManagerCommand);
}
