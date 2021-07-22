package com.blender.hub.computehub.core.manager.port.driven;

import com.blender.hub.computehub.core.manager.entity.Manager;

public interface ManagerProxyFactory {
    ManagerProxy buildManagerProxy(Manager manager);
}
