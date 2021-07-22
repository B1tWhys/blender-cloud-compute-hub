package com.blender.hub.computehub.core.manager.port.driven;

import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.FlamencoManager;

public interface ManagerInfraProxy {
    Hostname createInfraFor(FlamencoManager manager);
}
