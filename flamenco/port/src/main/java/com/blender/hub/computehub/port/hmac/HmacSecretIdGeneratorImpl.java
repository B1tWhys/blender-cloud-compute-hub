package com.blender.hub.computehub.port.hmac;

import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretIdGenerator;

import java.util.UUID;

public class HmacSecretIdGeneratorImpl implements HmacSecretIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
