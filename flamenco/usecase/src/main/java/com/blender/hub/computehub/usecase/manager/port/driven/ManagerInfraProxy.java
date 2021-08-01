package com.blender.hub.computehub.usecase.manager.port.driven;

import com.blender.hub.computehub.entity.manager.Hostname;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;

public interface ManagerInfraProxy {
    Hostname createInfraFor(FlamencoManager manager);
}
