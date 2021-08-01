package com.blender.hub.computehub.manager;

import com.blender.hub.computehub.entity.manager.Hostname;
import com.blender.hub.computehub.entity.manager.LinkingException;
import com.blender.hub.computehub.port.manager.FlamencoManagerProxyImpl;
import com.blender.hub.computehub.port.manager.ManagerLinkStartResponse;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FlamencoManagerProxyTest {
    public static final String MANAGER_HOSTNAME = "localhost";
    public static final int MANAGER_PORT = 1234;
    public static final String MANAGER_SCHEME = "https";
    public static final String HMAC_ID = "hmac-id";

    @Mock
    RestTemplate restTemplate;

    ResponseEntity<Object> responseEntity;

    Hostname hostname;

    ManagerProxy managerProxy;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setSecure(false);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        hostname = Hostname.builder()
                .scheme(MANAGER_SCHEME)
                .hostname(MANAGER_HOSTNAME)
                .port(MANAGER_PORT)
                .build();
        managerProxy = new FlamencoManagerProxyImpl(restTemplate, hostname);
    }

    @Test
    void linkStartUrlIsCorrect() {
        responseEntity = ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .body(new ManagerLinkStartResponse("http://localhost:8080/somepath"));
        when(restTemplate.getForEntity(Mockito.any(URI.class), Mockito.any())).thenReturn(responseEntity);

        managerProxy.exchangeHmacSecret();

        URI expectedUri = baseUriBuilder()
                .path("/setup/api/link-start")
                .queryParam("server", "http://localhost:8080")
                .build().toUri();
        verify(restTemplate).getForEntity(Mockito.eq(expectedUri), Mockito.any());
    }

    @Test
    void linkStartRequestErrorResultsInLinkingException() {
        when(restTemplate.getForEntity(Mockito.any(URI.class), Mockito.eq(ManagerLinkStartResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "some request error"));

        Assertions.assertThrows(LinkingException.class, managerProxy::exchangeHmacSecret);
    }

    @Test
    void hmacKeyExtractedFromRedirectUri() {
        responseEntity = ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .body(ManagerLinkStartResponse.builder().location(buildRedirectUrl().toASCIIString()).build());
        when(restTemplate.getForEntity(Mockito.any(), Mockito.any())).thenReturn(responseEntity);

        String actualHmacId = managerProxy.exchangeHmacSecret();

        assertThat(actualHmacId).isEqualTo(HMAC_ID);
    }

    @Test
    void linkingExceptionIfNoRedirectUri() {
        responseEntity = ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).build();
        when(restTemplate.getForEntity(Mockito.any(), Mockito.any())).thenReturn(responseEntity);

        Assertions.assertThrows(LinkingException.class, managerProxy::exchangeHmacSecret);
    }

    private URI buildRedirectUrl() {
        return UriComponentsBuilder.newInstance()
                .host("localhost")
                .port(1234)
                .path("/flamenco/managers/link/choose")
                .queryParam("identifier", HMAC_ID)
                // .queryParam("hmac", "some hmac value") // TODO: uncomment when ready to test this
                .build().toUri();
    }

    private UriComponentsBuilder baseUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme(MANAGER_SCHEME)
                .host(MANAGER_HOSTNAME)
                .port(MANAGER_PORT);
    }
}