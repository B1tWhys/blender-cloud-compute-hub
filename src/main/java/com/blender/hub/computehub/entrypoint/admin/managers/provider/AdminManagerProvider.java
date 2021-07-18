package com.blender.hub.computehub.entrypoint.admin.managers.provider;

import com.blender.hub.computehub.entrypoint.admin.managers.wire.AdminWireManager;

import java.util.List;

public interface AdminManagerProvider {
    List<AdminWireManager> listWireManagers();
}
