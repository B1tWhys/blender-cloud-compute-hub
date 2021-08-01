package com.blender.hub.computehub.usecase.hmac.port.driven;

public interface HmacSecretValueGenerator {
    /**
     * @return securely generated base64 encoded hmac secret
     */
    String generate();
}
