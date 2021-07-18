package com.blender.hub.computehub.core.mock;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecret;
import com.blender.hub.computehub.core.manager.entity.LinkingException;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxy;

public class MockManagerProxy implements ManagerProxy {
    public static final String MOCK_HMAC_SECRET_VALUE = "secret!";
    private final CreateHmacSecret createHmacSecret;
    public HmacSecret hmacSecret;
    private boolean linkingCompleted = false;
    
    public MockManagerProxy(CreateHmacSecret createHmacSecret) {
        this.createHmacSecret = createHmacSecret;
    }
    
    @Override
    public String exchangeHmacSecret() {
        hmacSecret = createHmacSecret.newHmacSecret(MOCK_HMAC_SECRET_VALUE);
        return hmacSecret.id;
    }
    
    @Override
    public void completeLinking() {
        if (hmacSecret == null) {
            throw new LinkingException("hmac not exchanged prior to completing linking");
        }
        linkingCompleted = true;
    }
    
    public boolean isLinked() {
        return (hmacSecret != null) && linkingCompleted;
    }
}
