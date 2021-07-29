package com.blender.hub.computehub.usecase.manager;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretValueGenerator;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacValidator;
import com.blender.hub.computehub.usecase.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.usecase.manager.usecase.CreateManagerImpl;
import com.blender.hub.computehub.usecase.manager.usecase.LinkManagerImpl;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LinkFlamencoManagerTest extends AbstractManagerLinkingTest {
    HmacSecretValueGenerator secretGenerator;

    @BeforeEach
    void setUp() {
        manager = buildUnlinkedManager();
        setupMockAdapters();
        initUseCase();
    }

    protected void setupMockAdapters() {
        when(hmacIdGenerator.generate()).thenReturn(SECRET_ID);
        when(proxyFactory.buildManagerProxy(any(FlamencoManager.class))).thenReturn(managerProxy);
        when(managerProxy.exchangeHmacSecret()).thenAnswer(i ->
                createHmacSecret.newLinkTimeHmacSecret(SECRET_VALUE).getId());
        when(hmacSecretRepository.getHmacSecret(SECRET_ID)).thenReturn(HmacSecret.builder()
                .id(SECRET_ID)
                .value(SECRET_VALUE).build());
    }

    protected void initUseCase() {
        linkManager = new LinkManagerImpl(proxyFactory, managerRepository, hmacSecretRepository);
        createHmacSecret = new CreateHmacSecretImpl(hmacIdGenerator, hmacSecretRepository, secretGenerator,
                hmacSecret -> Mockito.mock(HmacValidator.class));
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
        ArgumentCaptor<FlamencoManager> managerCaptor = ArgumentCaptor.forClass(FlamencoManager.class);
        verify(managerRepository, Mockito.atLeast(1)).upsert(managerCaptor.capture());

        List<FlamencoManager> managerVersions = managerCaptor.getAllValues();
        assertEquals(SECRET_ID, Optional.of(managerVersions.get(managerVersions.size()-1))
                .map(FlamencoManager::getHmacSecret)
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
