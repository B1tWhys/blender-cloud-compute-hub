package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.LinkingException;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class FlamencoManagerProxyImpl implements ManagerProxy {
    private static final String LINK_START_PATH = "/setup/api/link-start";

    private final RestTemplate restTemplate;
    private final Hostname managerHostname;

    @Override
    public String exchangeHmacSecret() {
        URI startLinkingUri = getHmacExchangeUrl();
        log.debug("hmac secret exchange request to hostname: {} full uri: {}", managerHostname, startLinkingUri.toASCIIString());

        ResponseEntity<ManagerLinkStartResponse> redirectResponse = null;
        try {
            redirectResponse = restTemplate.getForEntity(startLinkingUri, ManagerLinkStartResponse.class);
        } catch (RestClientException e) {
            throw new LinkingException("Error on start-link request: " + startLinkingUri.toASCIIString(), e);
        }

        URI redirect = Optional.of(redirectResponse)
                .map(ResponseEntity::getBody)
                .map(ManagerLinkStartResponse::getLocation)
                .map(UriComponentsBuilder::fromUriString)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::toUri)
                .orElseThrow();

        log.debug("hmac redirect: {}", redirect);

        // TODO: verify hmac value also passed in request
        return UriComponentsBuilder.fromUri(redirect).build().getQueryParams().getFirst("identifier");
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
