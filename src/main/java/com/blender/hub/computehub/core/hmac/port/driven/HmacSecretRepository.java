package com.blender.hub.computehub.core.hmac.port.driven;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;

public interface HmacSecretRepository {
    
    void storeHmacSecret(HmacSecret secret);
    
    HmacSecret getHmacSecret(String secretId);
}
