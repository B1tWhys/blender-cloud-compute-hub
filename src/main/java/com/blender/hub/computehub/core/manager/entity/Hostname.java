package com.blender.hub.computehub.core.manager.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;

@Value
@Builder
public class Hostname {
    String scheme;
    String hostname;
    int port;
}
