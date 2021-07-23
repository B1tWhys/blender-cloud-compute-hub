package com.blender.hub.computehub.core.manager.entity;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateManagerCommand {
    ManagerType managerType = ManagerType.FLAMENCO;
}
