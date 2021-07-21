package com.blender.hub.computehub.entrypoint.admin.hmac;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class AdminHmacProvider {
    private final HmacSecretRepository secretRepository;
    private final ManagerRepo managerRepo;

    List<AdminHmac> listHmacSecrets() {
        List<HmacSecret> secrets = secretRepository.getLatestHmacSecrets(5);
        return secrets.stream()
                .map(hs -> AdminHmac.builder()
                        .id(hs.getId())
                        .value(hs.getValue())
                        .managerId(managerRepo
                                .getByHmacId(hs.getId())
                                .map(Manager::getId)
                                .orElse("unlinked"))
                        .build())
                .collect(Collectors.toList());
    }
}
