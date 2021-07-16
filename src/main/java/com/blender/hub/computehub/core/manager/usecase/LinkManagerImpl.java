package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxyFactory;
import com.blender.hub.computehub.core.manager.port.driven.UpsertManager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinkManagerImpl implements LinkManager {
    private final ManagerProxyFactory proxyFactory;
    private final UpsertManager managerRepository;
    private final HmacSecretRepository hmacRepository;
    
    public void link(Manager manager) {
        ManagerProxy proxy = proxyFactory.buildManagerProxy(manager);
        String keyId = proxy.exchangeHmacSecret();
        manager.hmacSecret = hmacRepository.getHmacSecret(keyId);
        managerRepository.upsert(manager);
        proxy.completeLinking();
    }
}
