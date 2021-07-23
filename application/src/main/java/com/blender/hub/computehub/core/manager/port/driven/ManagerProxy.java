package com.blender.hub.computehub.core.manager.port.driven;

public interface ManagerProxy
{
    /**
     * tell the manager to generate an hmac key and exchange it with the server,
     * returning the id that the server generated for the hmac
     *
     * @return generated key id
     */
    String exchangeHmacSecret();
    
    void completeLinking();
}
