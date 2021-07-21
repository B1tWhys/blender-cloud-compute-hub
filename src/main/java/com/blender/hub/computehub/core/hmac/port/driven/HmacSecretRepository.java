package com.blender.hub.computehub.core.hmac.port.driven;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;

import java.util.List;

public interface HmacSecretRepository {
    
    void storeHmacSecret(HmacSecret secret);
    
    HmacSecret getHmacSecret(String secretId);

    List<HmacSecret> getLatestHmacSecrets(long limit);

}
