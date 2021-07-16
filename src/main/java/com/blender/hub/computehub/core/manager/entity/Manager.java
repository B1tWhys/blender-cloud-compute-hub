package com.blender.hub.computehub.core.manager.entity;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import lombok.Builder;

@Builder(toBuilder = true)
public class Manager {
    public final String id;
    public HmacSecret hmacSecret;
}
