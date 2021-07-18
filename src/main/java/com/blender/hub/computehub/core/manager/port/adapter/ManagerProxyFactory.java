package com.blender.hub.computehub.core.manager.port.adapter;

import com.blender.hub.computehub.core.manager.entity.Manager;

public interface ManagerProxyFactory {
    ManagerProxy buildManagerProxy(Manager manager);
}
