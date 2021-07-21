package com.blender.hub.computehub.adapter.hmac;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InMemoryHmacSecretRepoImpl implements HmacSecretRepository {
    private final Map<String, HmacSecret> secretMap = new HashMap<>();

    @Override
    public void storeHmacSecret(HmacSecret secret) {
        secretMap.put(secret.id, secret);
    }

    @Override
    public HmacSecret getHmacSecret(String secretId) {
        return secretMap.get(secretId);
    }

    @Override
    public List<HmacSecret> getLatestHmacSecrets(long limit) {
        return secretMap.values().stream().limit(limit).collect(Collectors.toList());
    }
}
