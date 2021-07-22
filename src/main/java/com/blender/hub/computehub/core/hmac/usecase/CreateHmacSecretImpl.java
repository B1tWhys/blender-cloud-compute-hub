package com.blender.hub.computehub.core.hmac.usecase;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.CreateHmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateHmacSecretImpl implements CreateHmacSecret {
    private final HmacSecretIdGenerator secretIdGenerator;
    private final HmacSecretRepository secretRepository;

    @Override
    public HmacSecret newLinkTimeHmacSecret(String secretValue) {
        HmacSecret newSecret = HmacSecret.builder()
                .id(secretIdGenerator.generate())
                .value(secretValue)
                .build();
        secretRepository.storeHmacSecret(newSecret);
        return newSecret;
    }
}
