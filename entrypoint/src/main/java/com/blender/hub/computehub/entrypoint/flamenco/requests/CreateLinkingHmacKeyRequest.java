package com.blender.hub.computehub.entrypoint.flamenco.requests;

import lombok.Data;

@Data
public class CreateLinkingHmacKeyRequest {
    private String key;

    @Override
    public String toString() {
        return "CreateLinkingHmacKeyRequest{" +
                "key='" + "xxxxxx" + '\'' +
                '}';
    }
}
