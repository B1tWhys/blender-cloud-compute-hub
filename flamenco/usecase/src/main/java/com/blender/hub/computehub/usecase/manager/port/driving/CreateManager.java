package com.blender.hub.computehub.usecase.manager.port.driving;


import com.blender.hub.computehub.entity.manager.CreateManagerCommand;
import com.blender.hub.computehub.entity.manager.Manager;

public interface CreateManager {
    Manager createManager(CreateManagerCommand createManagerCommand);
}
