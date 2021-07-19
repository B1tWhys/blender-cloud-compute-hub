package com.blender.hub.computehub.adapter.proxy.manager;

import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

@AllArgsConstructor
@Slf4j
public class ManagerProxyImpl implements ManagerProxy {
    public static final String LINK_START_PATH = "/setup/api/link-start";

    private final RestTemplate restTemplate;
    private final String managerHostname;

    @Override
    public String exchangeHmacSecret() {
        URI startLinkingUri = new DefaultUriBuilderFactory().builder()
                .host(managerHostname)
                .path(LINK_START_PATH)
                .build();

        ResponseEntity<Object> redirectResponse = restTemplate.getForEntity(startLinkingUri, Object.class);
        if (!redirectResponse.getStatusCode().is3xxRedirection()) {
            log.warn("Got a non-redirect response when attempting to exchange the HMAC key");
            // TODO: throw an exception & fail the initialization sequence
        } else {
            log.debug("hmac redirect: {}", redirectResponse.getHeaders().get("Location"));
        }

        return "";
    }

    @Override
    public void completeLinking() {
    }
}
