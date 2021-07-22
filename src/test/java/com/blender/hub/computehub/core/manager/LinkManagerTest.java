package com.blender.hub.computehub.core.manager;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.manager.entity.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LinkManagerTest extends AbstractManagerLinkingTest {
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        manager = buildUnlinkedManager();
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
