package com.blender.hub.computehub.entity.manager;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateManagerCommand {
    @Builder.Default
    ManagerType managerType = ManagerType.FLAMENCO;
}
