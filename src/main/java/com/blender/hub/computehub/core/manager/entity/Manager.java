package com.blender.hub.computehub.core.manager.entity;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import lombok.Builder;

import java.util.Date;

@Builder(toBuilder = true)
public class Manager {
    public final String id;
    public HmacSecret hmacSecret;
    private ManagerState state;
    public final Date createdTs;

    public ManagerState getState() {
        return state;
    }

    public void setState(ManagerState state) {
        this.state = state;
    }
}
