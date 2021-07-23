package com.blender.hub.computehub.port.hmac;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class InMemoryHmacSecretRepoImpl implements HmacSecretRepository {
    private static final ConcurrentMap<String, HmacSecret> secretMap = new ConcurrentHashMap<>();

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
