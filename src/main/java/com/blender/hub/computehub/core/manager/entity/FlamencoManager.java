package com.blender.hub.computehub.core.manager.entity;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class FlamencoManager extends Manager {
    private Hostname hostname;
    private HmacSecret hmacSecret;

    @Override
    public ManagerType getManagerType() {
        return ManagerType.FLAMENCO;
    }
}
