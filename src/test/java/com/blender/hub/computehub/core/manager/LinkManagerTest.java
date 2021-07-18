package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecret;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driving.LinkManager;
import com.blender.hub.computehub.core.manager.usecase.LinkManagerImpl;
import com.blender.hub.computehub.core.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkManagerTest {
    LinkManager useCase;
    MockManagerProxy mockManagerProxy;
    Manager manager;
    HmacSecretRepository hmacSecretRepository;
    MockHmacIdGenerator idGenerator;
    MockManagerProxyFactory proxyFactory;
    InMemoryManagerRepository managerRepository;
    
    @BeforeEach
    void setUp() {
        manager = buildUnlinkedManager();
        managerRepository = new InMemoryManagerRepository();
        managerRepository.upsert(manager);
        hmacSecretRepository = new InMemoryHmacSecretRepository();
        idGenerator = new MockHmacIdGenerator();
        CreateHmacSecret createHmacSecret = new CreateHmacSecret(idGenerator, hmacSecretRepository);
        proxyFactory = new MockManagerProxyFactory(createHmacSecret);
        mockManagerProxy = (MockManagerProxy) proxyFactory.buildManagerProxy(manager);
        useCase = new LinkManagerImpl(proxyFactory, managerRepository, hmacSecretRepository);
    }
    
    @Test
    void outboundLinkStartRequestHappens() {
        assertNull(mockManagerProxy.hmacSecret);
        useCase.link(manager);
        
        assertNotNull(mockManagerProxy.hmacSecret);
    }
    
    @Test
    void hmacCreatedWithGeneratedId() {
        assertNull(idGenerator.lastId);
        useCase.link(manager);
        
        assertNotNull(idGenerator.lastId);
        assertEquals(String.valueOf(idGenerator.lastId), mockManagerProxy.hmacSecret.id);
    }
    
    @Test
    void inboundHmacIsPersisted() {
        assertNull(mockManagerProxy.hmacSecret);
        
        useCase.link(manager);
        
        String hmacId = mockManagerProxy.hmacSecret.id;
        assertNotNull(hmacSecretRepository.getHmacSecret(hmacId));
    }
    
    @Test
    void inboundHmacValueIsPersisted() {
        useCase.link(manager);
        
        HmacSecret persistedSecret = hmacSecretRepository.getHmacSecret(mockManagerProxy.hmacSecret.id);
        assertEquals(MockManagerProxy.MOCK_HMAC_SECRET_VALUE, persistedSecret.value);
    }
    
    @Test
    void keyIsAssociatedWithManager() {
        useCase.link(manager);
        
        Manager currentManager = managerRepository.get(manager.getId()).orElse(null);
        assertNotNull(currentManager);
        assertNotNull(currentManager.getHmacSecret());
        assertEquals(mockManagerProxy.hmacSecret, currentManager.getHmacSecret());
    }
    
    @Test
    void managerLinkIsCompleted() {
        useCase.link(manager);
        
        assertTrue(mockManagerProxy.isLinked());
    }
    
    private Manager buildUnlinkedManager() {
        return Manager.builder()
                .id("some manager id")
                .build();
    }
}
