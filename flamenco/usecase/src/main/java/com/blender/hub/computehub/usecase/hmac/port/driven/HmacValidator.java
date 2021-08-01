package com.blender.hub.computehub.usecase.hmac.port.driven;

public interface HmacValidator {
    void validate(String message, String mac) throws AuthenticationException;
    void validate(String message, String padding, String mac) throws AuthenticationException;
}
