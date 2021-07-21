package com.blender.hub.computehub.entrypoint.manager;

import com.blender.hub.computehub.entrypoint.manager.requests.CreateLinkingHmacKeyRequest;
import com.blender.hub.computehub.entrypoint.manager.response.CreateLinkingHmacKeyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/flamenco/managers")
public class ManagerController {
    ManagerLinkingProvider managerLinkingProvider;

    @PostMapping("/link/exchange")
    public CreateLinkingHmacKeyResponse createLinkingHmac(CreateLinkingHmacKeyRequest createLinkingHmacKeyRequest) {
        return managerLinkingProvider.createLinkingHmac(createLinkingHmacKeyRequest);
    }
}
