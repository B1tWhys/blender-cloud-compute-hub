package com.blender.hub.computehub.core.manager.port.driven;

import com.blender.hub.computehub.core.manager.entity.FlamencoManager;

public interface ManagerProxyFactory {
    ManagerProxy buildManagerProxy(FlamencoManager manager);
}
