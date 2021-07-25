package com.blender.hub.computehub.usecase.manager.usecase;

import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxyFactory;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.usecase.manager.port.driving.LinkManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LinkManagerImpl implements LinkManager {
    private final ManagerProxyFactory proxyFactory;
    private final ManagerRepo managerRepository;
    private final HmacSecretRepository hmacRepository;
    
    public void link(FlamencoManager manager) {
        ManagerProxy proxy = exchangeLinkingHmacSecret(manager);
        managerRepository.upsert(manager);
        completeLinking(proxy);
    }

    private ManagerProxy exchangeLinkingHmacSecret(FlamencoManager manager) {
        ManagerProxy proxy = proxyFactory.buildManagerProxy(manager);
        String keyId = proxy.exchangeHmacSecret(); // TODO: verify hmac value lines up
        manager.setHmacSecret(hmacRepository.getHmacSecret(keyId));
        log.info("manager {} linked to hmac secret id: {}", manager.getId(), keyId);
        return proxy;
    }

    private void completeLinking(ManagerProxy proxy) {
        proxy.completeLinking();
    }
}
