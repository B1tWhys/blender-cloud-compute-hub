package com.blender.hub.computehub.manager;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.port.hmac.HmacValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HmacValidatorImplTest {
    HmacValidatorImpl validator;
    HmacSecret secret;

    @BeforeEach
    void setUp() {
        secret = HmacSecret.builder()
                .id("9cbb53d7-7f23-4a53-8ff8-b976814fd0b5")
                .value("d091f27aaa7fb0561327b7f475e334dca112c1785d522310263c640d6042d5fa")
                .build();
        validator = new HmacValidatorImpl(secret);
    }

    @Test
    public void validHmacTest() {
        Assertions.assertDoesNotThrow(() ->
                validator.validate("http://localhost:49180/setup/link-return",
                        "8260e1dd246785025c80aa0ae422a5223e7d091f45adfa026dda2357206183b4"));
    }

    @Test
    public void invalidHmacTest() {
        validator = new HmacValidatorImpl(secret);
        Assertions.assertThrows(Throwable.class, () ->
                validator.validate("foo",
                        "8260e1dd246785025c80aa0ae422a5223e7d091f45adfa026dda2357206183b4"));
    }
}
