package com.blender.hub.computehub.entrypoint.flamenco;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.entrypoint.flamenco.requests.CreateLinkingHmacKeyRequest;
import com.blender.hub.computehub.entrypoint.flamenco.response.CreateLinkingHmacKeyResponse;
import com.blender.hub.computehub.usecase.hmac.usecase.CreateHmacSecretImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ManagerLinkingProvider {
    CreateHmacSecretImpl createHmacSecret;

    CreateLinkingHmacKeyResponse createLinkingHmac(CreateLinkingHmacKeyRequest request) {
        HmacSecret secret = createHmacSecret.newLinkTimeHmacSecret(request.getKey());
        log.debug("New linking hmac secret created with id: {}", secret.id);
        return CreateLinkingHmacKeyResponse.builder().identifier(secret.id).build();
    }
}
