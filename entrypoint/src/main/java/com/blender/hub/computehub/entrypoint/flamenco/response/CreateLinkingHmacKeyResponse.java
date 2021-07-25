package com.blender.hub.computehub.entrypoint.flamenco.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLinkingHmacKeyResponse {
    private String identifier;
}
