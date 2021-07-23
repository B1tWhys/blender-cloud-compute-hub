package com.blender.hub.computehub.port.manager;

import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxyFactory;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class ManagerProxyFactoryImpl implements ManagerProxyFactory {
    RestTemplate noRedirectRestTemplate;

    @Override
    public ManagerProxy buildManagerProxy(FlamencoManager manager) {
        return new FlamencoManagerProxyImpl(noRedirectRestTemplate, manager.getHostname());
    }
}
