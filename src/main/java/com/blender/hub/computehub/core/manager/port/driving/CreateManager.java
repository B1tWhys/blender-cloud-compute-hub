package com.blender.hub.computehub.core.manager.port.driving;


import com.blender.hub.computehub.core.manager.entity.CreateManagerCommand;
import com.blender.hub.computehub.core.manager.entity.Manager;

public interface CreateManager {
    Manager createManager(CreateManagerCommand createManagerCommand);
}
