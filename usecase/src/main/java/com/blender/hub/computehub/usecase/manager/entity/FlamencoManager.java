package com.blender.hub.computehub.usecase.manager.entity;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.entity.manager.Hostname;
import com.blender.hub.computehub.entity.manager.Manager;
import com.blender.hub.computehub.entity.manager.ManagerType;
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
