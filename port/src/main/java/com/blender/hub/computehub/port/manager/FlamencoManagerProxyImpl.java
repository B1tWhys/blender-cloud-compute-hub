package com.blender.hub.computehub.port.manager;

import com.blender.hub.computehub.entity.manager.Hostname;
import com.blender.hub.computehub.entity.manager.LinkingException;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Slf4j
public class FlamencoManagerProxyImpl implements ManagerProxy {
    private static final String LINK_START_PATH = "/setup/api/link-start";

    private final RestTemplate restTemplate;
    private final Hostname managerHostname;

    @Override
    public String exchangeHmacSecret() {
        URI startLinkingUri = getHmacExchangeUrl();
        ResponseEntity<ManagerLinkStartResponse> response = makeLinkStartRequest(startLinkingUri);
        String keyId = extractKeyId(response);
        // TODO: extract & verify hmac value that should also have been returned
        return keyId;
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

    private ResponseEntity<ManagerLinkStartResponse> makeLinkStartRequest(URI startLinkingUri) {
        log.debug("making link start request to hostname: {} full uri: {}",
                managerHostname, startLinkingUri.toASCIIString());
        ResponseEntity<ManagerLinkStartResponse> response;
        try {
            response = restTemplate.getForEntity(startLinkingUri, ManagerLinkStartResponse.class);
        } catch (RestClientException e) {
            log.warn("flamenco manager link start request to {} failed with exception: ", startLinkingUri.toASCIIString(), e);
            throw new LinkingException("Error on start-link request: " + startLinkingUri.toASCIIString(), e);
        }
        return response;
    }

    private String extractKeyId(ResponseEntity<ManagerLinkStartResponse> redirectResponse) {
        URI redirect = Optional.of(redirectResponse)
                .map(ResponseEntity::getBody)
                .map(ManagerLinkStartResponse::getLocation)
                .map(UriComponentsBuilder::fromUriString)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::toUri)
                .orElseThrow(() -> new LinkingException("Error extracting redirect URI from hmac secret exchange response"));
        log.debug("hmac redirect: {}", redirect);
        return UriComponentsBuilder.fromUri(redirect).build().getQueryParams().getFirst("identifier");
    }

    @Override
    public void completeLinking() {
    }
}
