package com.blender.hub.computehub.usecase.manager.port.driven;

import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;

public interface ManagerProxyFactory {
    ManagerProxy buildManagerProxy(FlamencoManager manager);
}
