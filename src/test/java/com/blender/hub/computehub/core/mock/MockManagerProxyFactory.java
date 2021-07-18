package com.blender.hub.computehub.core.mock;

import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecret;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxyFactory;

import java.util.HashMap;
import java.util.Map;

public class MockManagerProxyFactory implements ManagerProxyFactory {
    public Map<Manager, ManagerProxy> proxies = new HashMap<>();
    private final CreateHmacSecret createHmacSecret;
    
    public MockManagerProxyFactory(CreateHmacSecret createHmacSecret) {
        this.createHmacSecret = createHmacSecret;
    }
    
    @Override
    public ManagerProxy buildManagerProxy(Manager manager) {
        if (proxies.containsKey(manager)) {
            return proxies.get(manager);
        } else {
            ManagerProxy proxy = new MockManagerProxy(createHmacSecret);
            proxies.put(manager, proxy);
            return proxy;
        }
    }
}
