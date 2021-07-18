package com.blender.hub.computehub.core.manager.port.adapter;

import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;

public interface ManagerInfraProxy {
    Hostname createInfraFor(Manager manager);
}
