package com.blender.hub.computehub.usecase.manager.port.driving;


import com.blender.hub.computehub.entity.manager.CreateManagerCommand;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;

public interface CreateManager {
    FlamencoManager createManager(CreateManagerCommand createManagerCommand);
}
