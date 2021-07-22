package com.blender.hub.computehub.adapter.proxy.manager;

import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerProxy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class ManagerProxyImpl implements ManagerProxy {
    private static final String LINK_START_PATH = "/setup/api/link-start";

    private final RestTemplate restTemplate;
    private final Hostname managerHostname;

    @Override
    public String exchangeHmacSecret() {
        URI startLinkingUri = getHmacExchangeUrl();
        log.debug("hmac secret exchange request to hostname: {} full uri: {}", managerHostname, startLinkingUri.toASCIIString());

        ResponseEntity<Object> redirectResponse = restTemplate.getForEntity(startLinkingUri, Object.class);
        if (!redirectResponse.getStatusCode().is3xxRedirection()) {
            log.warn("Got a non-redirect response when attempting to exchange the HMAC key");
            // TODO: throw an exception & fail the initialization sequence
            return null;
        }

        URI redirect = Optional.of(redirectResponse)
                .map(HttpEntity::getHeaders)
                .map(HttpHeaders::getLocation)
                .orElseThrow();
        log.debug("hmac redirect: {}", redirect);

        String hmacId = UriComponentsBuilder.fromUri(redirect).build().getQueryParams().getFirst("identifier");
        // TODO: verify hmac value also passed in request
        return hmacId;
    }

    private URI getHmacExchangeUrl() {
        String serverUrl = getServerUrl();
        return UriComponentsBuilder.newInstance()
                .scheme(managerHostname.getScheme())
                .host(managerHostname.getHostname())
                .port(managerHostname.getPort())
                .path(LINK_START_PATH)
                .queryParam("server", serverUrl)
                .build().toUri();
    }

    private String getServerUrl() {
        Hostname serverHostname = getServerHostname();
        return UriComponentsBuilder.newInstance()
                .scheme(serverHostname.getScheme())
                .host(serverHostname.getHostname())
                .port(serverHostname.getPort())
                .build().toUri().toASCIIString();
    }

    private Hostname getServerHostname() {
        HttpServletRequest request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow();
        return Hostname.builder()
                .scheme(request.getScheme())
                .hostname(request.getServerName())
                .port(request.getServerPort())
                .build();
    }

    @Override
    public void completeLinking() {
    }
}
