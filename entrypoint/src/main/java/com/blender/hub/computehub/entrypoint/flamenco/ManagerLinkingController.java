package com.blender.hub.computehub.entrypoint.flamenco;

import com.blender.hub.computehub.entrypoint.flamenco.requests.CreateLinkingHmacKeyRequest;
import com.blender.hub.computehub.entrypoint.flamenco.response.CreateLinkingHmacKeyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/flamenco/managers")
public class ManagerLinkingController {
    ManagerLinkingProvider managerLinkingProvider;

    @PostMapping("/link/exchange")
    public CreateLinkingHmacKeyResponse createLinkingHmac(@RequestBody CreateLinkingHmacKeyRequest createLinkingHmacKeyRequest) {
        return managerLinkingProvider.createLinkingHmac(createLinkingHmacKeyRequest);
    }
}
