package com.blender.hub.computehub.usecase.hmac.port.driven;

import com.blender.hub.computehub.entity.hmac.HmacSecret;

public interface HmacValidatorFactory {
    HmacValidator buildValidator(HmacSecret hmacSecret);
}
