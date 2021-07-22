package com.blender.hub.computehub.core.manager.entity;

public class LinkingException extends RuntimeException {
    public LinkingException(String message) {
        super(message);
    }

    public LinkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
