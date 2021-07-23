package com.blender.hub.computehub.entrypoint.admin.hmac;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacSecretRepository;
import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerRepo;
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
                                .map(FlamencoManager::getId)
                                .orElse("unlinked"))
                        .build())
                .collect(Collectors.toList());
    }
}
