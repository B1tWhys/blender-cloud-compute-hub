package com.blender.hub.computehub.usecase.hmac.usecase;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class HmacResetCommand { // TODO: figure out a better package structure for this
    String managerId;
    String padding;
    String hmac;
}
