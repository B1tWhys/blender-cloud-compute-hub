package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.adapter.manager.ManagerProxyFactoryImpl;
import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecret;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.driven.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.manager.usecase.LinkManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractManagerLinkingTest {
    protected static final String SECRET_ID = "secret id";
    protected static final String SECRET_VALUE = "secret value";
    protected static final String MANAGER_ID = "manager id";
    protected static final String MANAGER_HOSTNAME = "managerHostname";

    protected LinkManager linkManager;

    protected CreateManager createManager;

    protected CreateHmacSecret createHmacSecret;

    protected Manager manager;

    @Mock
    protected ManagerProxy managerProxy;

    @Mock
    protected HmacSecretIdGenerator hmacIdGenerator;

    @Mock
    protected ManagerIdGenerator managerIdGenerator;

    @Mock
    protected ManagerProxyFactoryImpl proxyFactory;

    @Mock
    protected ManagerRepo managerRepository;

    @Mock
    protected HmacSecretRepository hmacSecretRepository;

    @Mock
    protected ManagerInfraProxy managerInfraProxy;

    @BeforeEach
    void setUp() {
        setupUseCases();
        setupMockAdapters();
    }

    private void setupMockAdapters() {
        when(hmacIdGenerator.generate()).thenReturn(SECRET_ID);
        when(managerIdGenerator.generate()).thenReturn(MANAGER_ID);
        when(proxyFactory.buildManagerProxy(any(Manager.class))).thenReturn(managerProxy);
        when(managerProxy.exchangeHmacSecret()).thenAnswer(i ->
                createHmacSecret.newHmacSecret(SECRET_VALUE).getId());
        when(hmacSecretRepository.getHmacSecret(SECRET_ID)).thenReturn(HmacSecret.builder()
                .id(SECRET_ID)
                .value(SECRET_VALUE).build());
        when(managerInfraProxy.createInfraFor(any(Manager.class)))
                .thenReturn(Hostname.builder().scheme("http").hostname(MANAGER_HOSTNAME).port(1234).build());
    }

    private void setupUseCases() {
        linkManager = new LinkManagerImpl(proxyFactory, managerRepository, hmacSecretRepository);
        createHmacSecret = new CreateHmacSecret(hmacIdGenerator, hmacSecretRepository);
        createManager = new CreateManagerImpl(managerIdGenerator, managerRepository, managerInfraProxy, linkManager);
    }

    protected Manager buildUnlinkedManager() {
        return Manager.builder()
                .id(MANAGER_ID)
                .build();
    }
}
