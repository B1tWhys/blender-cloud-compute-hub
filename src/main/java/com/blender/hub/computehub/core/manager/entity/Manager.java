package com.blender.hub.computehub.core.manager.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class Manager {
    protected final String id;
    protected ManagerState state;
    protected final long createdTs;
}
