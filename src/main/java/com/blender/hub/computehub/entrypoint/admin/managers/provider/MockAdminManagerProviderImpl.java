package com.blender.hub.computehub.entrypoint.admin.managers.provider;

import com.blender.hub.computehub.entrypoint.admin.managers.wire.AdminWireManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class MockAdminManagerProviderImpl implements AdminManagerProvider {
    public List<AdminWireManager> listWireManagers(int limit) {
        log.info("returning mocked list of managers");
        return List.of(
                AdminWireManager.builder().id("foo").state("NEW").humanReadableCreatedTs("yesterday").build(),
                AdminWireManager.builder().id("bar").state("PENDING_HMAC").humanReadableCreatedTs("today").build(),
                AdminWireManager.builder().id("bar").state("DELETING").humanReadableCreatedTs("whenever").build());
    }

    @Override
    public void create() {
        log.info("pretending to create a manager");
    }
}
