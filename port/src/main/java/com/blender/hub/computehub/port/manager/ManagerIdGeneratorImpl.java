package com.blender.hub.computehub.port.manager;

import com.blender.hub.computehub.usecase.manager.port.driven.ManagerIdGenerator;

import java.util.UUID;

public class ManagerIdGeneratorImpl implements ManagerIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
