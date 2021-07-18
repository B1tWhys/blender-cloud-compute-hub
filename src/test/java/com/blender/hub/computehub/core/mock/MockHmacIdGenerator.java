package com.blender.hub.computehub.core.mock;

import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretIdGenerator;

public class MockHmacIdGenerator implements HmacSecretIdGenerator {
    public Integer nextId = 0;
    public Integer lastId = null;
    
    @Override
    public String generate() {
        String ret = String.valueOf(nextId);
        lastId = nextId;
        nextId += 1;
        return ret;
    }
    
    public String getLastIdStr() {
        return String.valueOf(lastId);
    }
}
