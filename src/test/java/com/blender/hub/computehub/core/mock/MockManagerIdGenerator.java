package com.blender.hub.computehub.core.mock;


import com.blender.hub.computehub.core.manager.port.adapter.ManagerIdGenerator;

public class MockManagerIdGenerator implements ManagerIdGenerator {
    public Integer nextManagerId = 0;
    public Integer lastManagerId = null;
    
    @Override
    public String generate() {
        String ret = String.valueOf(nextManagerId);
        lastManagerId = nextManagerId;
        nextManagerId += 1;
        return ret;
    }
}
