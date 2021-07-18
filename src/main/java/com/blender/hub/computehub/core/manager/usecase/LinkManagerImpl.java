package com.blender.hub.computehub.core.manager.usecase;

import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxyFactory;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinkManagerImpl implements LinkManager {
    private final ManagerProxyFactory proxyFactory;
    private final ManagerRepo managerRepository;
    private final HmacSecretRepository hmacRepository;
    
    public void link(Manager manager) {
        ManagerProxy proxy = proxyFactory.buildManagerProxy(manager);
        String keyId = proxy.exchangeHmacSecret();
        manager.setHmacSecret(hmacRepository.getHmacSecret(keyId));
        managerRepository.upsert(manager);
        proxy.completeLinking();
    }
}
