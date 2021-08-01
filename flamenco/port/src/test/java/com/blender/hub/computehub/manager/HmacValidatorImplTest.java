package com.blender.hub.computehub.manager;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.port.hmac.HmacValidatorImpl;
import com.blender.hub.computehub.usecase.hmac.port.driven.AuthenticationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HmacValidatorImplTest {
    public static final String SECRET_ID = "9cbb53d7-7f23-4a53-8ff8-b976814fd0b5";
    public static final String SECRET_VALUE = "d091f27aaa7fb0561327b7f475e334dca112c1785d522310263c640d6042d5fa";
    public static final String VALID_MAC = "8260e1dd246785025c80aa0ae422a5223e7d091f45adfa026dda2357206183b4";
    public static final String VALID_MESSAGE = "http://localhost:49180/setup/link-return";
    HmacValidatorImpl validator;
    HmacSecret secret;

    @BeforeEach
    void setUp() {
        secret = HmacSecret.builder()
                .id(SECRET_ID)
                .value(SECRET_VALUE)
                .build();
        validator = new HmacValidatorImpl(secret);
    }

    @Test
    public void validHmacTest() {
        Assertions.assertDoesNotThrow(() ->
                validator.validate(VALID_MESSAGE,
                        VALID_MAC));
    }

    @Test
    public void invalidHmacTest() {
        Assertions.assertThrows(AuthenticationException.class, () ->
                validator.validate("foo",
                        VALID_MAC));
    }

    @Test
    public void nonHexHmacValueTest() {
        secret = HmacSecret.builder()
                .id(SECRET_ID)
                .value("zzzzz")
                .build();
        validator = new HmacValidatorImpl(secret);
        Assertions.assertThrows(AuthenticationException.class, () ->
                validator.validate(VALID_MESSAGE, VALID_MAC));
    }

    @Test
    public void nonHexMacTest() {
        Assertions.assertThrows(AuthenticationException.class, () ->
                validator.validate(VALID_MESSAGE, "ghijklmnop"));
    }
}
