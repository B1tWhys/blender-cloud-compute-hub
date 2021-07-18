package com.blender.hub.computehub.entrypoint.admin.managers.provider;

import com.blender.hub.computehub.entrypoint.admin.managers.wire.AdminWireManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MockAdminManagerProviderImpl implements AdminManagerProvider {
    public List<AdminWireManager> listWireManagers() {
        return List.of(
                AdminWireManager.builder().id("foo").state("NEW").humanReadableCreatedTs("yesterday").build(),
                AdminWireManager.builder().id("bar").state("PENDING_HMAC").humanReadableCreatedTs("today").build(),
                AdminWireManager.builder().id("bar").state("DELETING").humanReadableCreatedTs("whenever").build());
    }
}
