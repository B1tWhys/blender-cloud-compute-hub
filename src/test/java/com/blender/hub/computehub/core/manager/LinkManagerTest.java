package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecret;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.core.manager.usecase.LinkManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class LinkManagerTest extends AbstractManagerLinkingTest {
    @BeforeEach
    void setUp() {
        manager = buildUnlinkedManager();
        setupMockAdapters();
        initUseCase();
    }

    protected void setupMockAdapters() {
        when(hmacIdGenerator.generate()).thenReturn(SECRET_ID);
        when(proxyFactory.buildManagerProxy(any(Manager.class))).thenReturn(managerProxy);
        when(managerProxy.exchangeHmacSecret()).thenAnswer(i ->
                createHmacSecret.newHmacSecret(SECRET_VALUE).getId());
        when(hmacSecretRepository.getHmacSecret(SECRET_ID)).thenReturn(HmacSecret.builder()
                .id(SECRET_ID)
                .value(SECRET_VALUE).build());
    }

    protected void initUseCase() {
        linkManager = new LinkManagerImpl(proxyFactory, managerRepository, hmacSecretRepository);
        createHmacSecret = new CreateHmacSecret(hmacIdGenerator, hmacSecretRepository);
        createManager = new CreateManagerImpl(managerIdGenerator, managerRepository, managerInfraProxy, linkManager, timeProvider);
    }

    @Test
    void outboundLinkStartRequestHappens() {
        linkManager.link(manager);
        verify(managerProxy, times(1)).exchangeHmacSecret();
    }
    
    @Test
    void hmacCreatedWithGeneratedId() {
        linkManager.link(manager);
        HmacSecret storedSecret = getStoredHmacSecret();
        assertEquals(SECRET_ID, storedSecret.getId());
    }

    @Test
    void inboundHmacIsPersisted() {
        linkManager.link(manager);
        HmacSecret storedSecret = getStoredHmacSecret();
        assertEquals(SECRET_VALUE, storedSecret.getValue());
    }

    @Test
    void keyIsAssociatedWithManager() {
        linkManager.link(manager);
        ArgumentCaptor<Manager> managerCaptor = ArgumentCaptor.forClass(Manager.class);
        verify(managerRepository, Mockito.atLeast(1)).upsert(managerCaptor.capture());

        List<Manager> managerVersions = managerCaptor.getAllValues();
        assertEquals(SECRET_ID, Optional.of(managerVersions.get(managerVersions.size()-1))
                .map(Manager::getHmacSecret)
                .map(HmacSecret::getId)
                .orElseThrow());
    }

    @Test
    void managerLinkIsCompleted() {
        linkManager.link(manager);

        verify(managerProxy).completeLinking();
    }

    private HmacSecret getStoredHmacSecret() {
        ArgumentCaptor<HmacSecret> secretCaptor = ArgumentCaptor.forClass(HmacSecret.class);
        verify(hmacSecretRepository, times(1)).storeHmacSecret(secretCaptor.capture());
        return secretCaptor.getValue();
    }
}
