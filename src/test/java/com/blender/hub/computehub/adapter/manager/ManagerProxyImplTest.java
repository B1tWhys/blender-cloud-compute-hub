package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerProxyImplTest {
    public static final String MANAGER_HOSTNAME = "localhost";
    public static final int MANAGER_PORT = 1234;
    public static final String MANAGER_SCHEME = "https";

    @Mock
    RestTemplate restTemplate;

    @Mock
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
        managerProxy = new ManagerProxyImpl(restTemplate, hostname);
        when(restTemplate.getForEntity(any(), any())).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new ManagerLinkStartResponse("http://localhost:8080/somepath"));
    }

    @Test
    void linkRequestUrlTest() {
        managerProxy.exchangeHmacSecret();

        URI expectedUri = baseUriBuilder()
                .path("/setup/api/link-start")
                .queryParam("server", "http://localhost:8080")
                .build().toUri();
        verify(restTemplate).getForEntity(Mockito.eq(expectedUri), any());
    }

    private UriComponentsBuilder baseUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme(MANAGER_SCHEME)
                .host(MANAGER_HOSTNAME)
                .port(MANAGER_PORT);
    }
}