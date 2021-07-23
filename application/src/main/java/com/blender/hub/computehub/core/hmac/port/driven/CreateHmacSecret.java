package com.blender.hub.computehub.core.hmac.port.driven;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;

public interface CreateHmacSecret {
    /**
     * Handle secret sent by a flamenco manager during linking
     * @param secretValue value of the secret to be created
     * @return generated sec
     */
    HmacSecret newLinkTimeHmacSecret(String secretValue);
}
