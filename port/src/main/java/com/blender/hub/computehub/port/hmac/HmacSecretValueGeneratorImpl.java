package com.blender.hub.computehub.port.hmac;

import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretValueGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@AllArgsConstructor
public class HmacSecretValueGeneratorImpl implements HmacSecretValueGenerator {
    private static final int SECRET_LEN = 128;
    SecureRandom secureRandom;

    @Override
    public String generate() {
        byte[] buff = new byte[SECRET_LEN];
        secureRandom.nextBytes(buff);
        return Base64.getEncoder().encodeToString(buff);
    }
}
