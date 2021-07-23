package com.blender.hub.computehub.usecase.hmac.usecase;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.CreateHmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretIdGenerator;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
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
