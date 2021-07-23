package com.blender.hub.computehub.entity.manager;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateManagerCommand {
    ManagerType managerType = ManagerType.FLAMENCO;
}
