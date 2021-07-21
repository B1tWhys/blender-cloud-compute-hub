package com.blender.hub.computehub.core.mock;


import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryHmacSecretRepository implements HmacSecretRepository {
    Map<String, HmacSecret> hmacSecretMap = new HashMap<>();
    
    @Override
    public void storeHmacSecret(HmacSecret secret) {
        hmacSecretMap.put(secret.id, secret);
    }
    
    @Override
    public HmacSecret getHmacSecret(String secretId) {
        return hmacSecretMap.get(secretId);
    }

    @Override
    public List<HmacSecret> getLatestHmacSecrets(long limit) {
        return hmacSecretMap.values().stream().limit(limit).collect(Collectors.toList());
    }
}
