package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.core.manager.port.adapter.ManagerIdGenerator;

import java.util.UUID;

public class ManagerIdGeneratorImpl implements ManagerIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
