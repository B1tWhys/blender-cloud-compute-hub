package com.blender.hub.computehub.usecase.hmac.usecase;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.CreateHmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretGenerator;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CreateHmacSecretImpl implements CreateHmacSecret {
    private final HmacSecretIdGenerator secretIdGenerator;
    private final HmacSecretRepository secretRepository;
    private final HmacSecretGenerator hmacValueGenerator;

    @Override
    public HmacSecret newLinkTimeHmacSecret(String secretValue) {
        HmacSecret newSecret = HmacSecret.builder()
                .id(secretIdGenerator.generate())
                .value(secretValue)
                .build();
        secretRepository.storeHmacSecret(newSecret);
        return newSecret;
    }

    @Override
    public HmacSecret refresh(HmacSecret oldSecret) {
        HmacSecret newSecret = HmacSecret.builder()
                .id(secretIdGenerator.generate())
                .value(hmacValueGenerator.generate())
                .build();
        secretRepository.storeHmacSecret(newSecret);
        try {
            secretRepository.deleteSecret(oldSecret.getId());
        } catch (Exception e) {
            log.warn("Old secret: {} failed to delete during hmac refresh!", oldSecret.getId(), e);
            // don't need to do anything, since the secret will be cleaned up later once it expires
        }
        return newSecret;
    }
}
