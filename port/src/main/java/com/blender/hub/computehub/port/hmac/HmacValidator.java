package com.blender.hub.computehub.port.hmac;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.AuthenticationException;
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
public class HmacValidator {
    HmacSecret secret;

    public void validate(String message, String mac) throws AuthenticationException, DecoderException {
        byte[] actualDigest = digestOf(message);
        byte[] expectedDigest = Hex.decodeHex(mac);
        if (!MessageDigest.isEqual(actualDigest, expectedDigest)) {
            log.debug("Hmac digest mismatch with secret id {}. Expected hmac: {}, actual hmac: {}", secret.getId(),
                    Hex.encodeHexString(expectedDigest), Hex.encodeHexString(actualDigest));
            throw new AuthenticationException("Validation failed with hmac secret id: " + secret.getId());
        }
    }

    private byte[] digestOf(String rawMsg) throws DecoderException {
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, Hex.decodeHex(secret.getValue()));
        byte[] message = (secret.getId() + "-" + rawMsg).getBytes(StandardCharsets.UTF_8);
        return hmacUtils.hmac(message);
    }
}
