package com.blender.hub.computehub.port.hmac;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.AuthenticationException;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@AllArgsConstructor
@Slf4j
public class HmacValidatorImpl implements HmacValidator {
    HmacSecret secret;

    public void validate(String message, String mac) throws AuthenticationException {
        byte[] actualDigest = digest(mac);
        byte[] expectedDigest = macToHex(mac);

        if (!MessageDigest.isEqual(actualDigest, expectedDigest)) {
            log.debug("Hmac digest mismatch with secret id {}. Expected hmac: {}, actual hmac: {}", secret.getId(),
                    Hex.encodeHexString(expectedDigest), Hex.encodeHexString(actualDigest));
            throw new AuthenticationException("Validation failed with hmac secret id: " + secret.getId());
        }
    }

    private byte[] macToHex(String mac) throws AuthenticationException {
        byte[] expectedDigest;
        try {
            expectedDigest = Hex.decodeHex(mac);
        } catch (DecoderException e) {
            throw new AuthenticationException("invalid mac: " + mac, e);
        }
        return expectedDigest;
    }

    private byte[] digest(String rawMsg) throws AuthenticationException {
        byte[] key;
        try {
            key = Hex.decodeHex(secret.getValue());
        } catch (DecoderException e) {
            throw new AuthenticationException("invalid hmac secret for hmac id: " + secret.getId());
        }
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key);
        byte[] message = (secret.getId() + "-" + rawMsg).getBytes(StandardCharsets.UTF_8);
        return hmacUtils.hmac(message);
    }
}
