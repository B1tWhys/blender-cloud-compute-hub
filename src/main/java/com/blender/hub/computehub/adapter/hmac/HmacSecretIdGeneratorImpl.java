package com.blender.hub.computehub.adapter.hmac;

import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretIdGenerator;

import java.util.UUID;

public class HmacSecretIdGeneratorImpl implements HmacSecretIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
