package com.blender.hub.computehub.usecase.hmac.port.driven;


import com.blender.hub.computehub.entity.hmac.HmacSecret;

import java.util.List;

public interface HmacSecretRepository {
    
    void storeHmacSecret(HmacSecret secret);
    
    HmacSecret getHmacSecret(String secretId);

    List<HmacSecret> getLatestHmacSecrets(long limit);

    void deleteSecret(String secretId);
}
