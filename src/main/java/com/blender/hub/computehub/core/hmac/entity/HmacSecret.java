package com.blender.hub.computehub.core.hmac.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class HmacSecret {
    public final String id;
    public final String value;
}
