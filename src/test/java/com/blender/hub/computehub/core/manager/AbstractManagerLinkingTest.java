package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.adapter.manager.ManagerProxyFactoryImpl;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.core.manager.entity.FlamencoManager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerIdGenerator;
import com.blender.hub.computehub.core.manager.port.driven.ManagerInfraProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerRepo;
import com.blender.hub.computehub.core.manager.port.driving.CreateManager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import com.blender.hub.computehub.core.util.TimeProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractManagerLinkingTest {
    protected static final String SECRET_ID = "secret id";
    protected static final String SECRET_VALUE = "secret value";
    protected static final String MANAGER_ID = "manager id";
    protected static final String MANAGER_HOSTNAME = "managerHostname";
    protected static final long NOW_TS = 5000L;

    protected LinkManager linkManager;

    protected CreateManager createManager;

    protected CreateHmacSecretImpl createHmacSecret;

    protected FlamencoManager manager;

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

    @Mock
    protected TimeProvider timeProvider;


    protected FlamencoManager buildUnlinkedManager() {
        return FlamencoManager.builder()
                .id(MANAGER_ID)
                .build();
    }
}
