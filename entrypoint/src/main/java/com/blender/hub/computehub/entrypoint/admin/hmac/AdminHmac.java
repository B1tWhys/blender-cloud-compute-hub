package com.blender.hub.computehub.entrypoint.admin.hmac;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminHmac {
    private String id;
    private String value;
    private String managerId;
}
