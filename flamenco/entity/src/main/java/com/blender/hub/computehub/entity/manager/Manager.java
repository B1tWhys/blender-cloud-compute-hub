package com.blender.hub.computehub.entity.manager;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class Manager {
    protected final String id;
    protected ManagerState state;
    protected final long createdTs;

    public abstract ManagerType getManagerType();
}
