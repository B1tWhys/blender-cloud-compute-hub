package com.blender.hub.computehub.entity.hmac;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@EqualsAndHashCode
@Value
public class HmacSecret {
    public String id;
    public String value;
}
