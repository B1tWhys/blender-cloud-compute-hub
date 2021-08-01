package com.blender.hub.computehub.application.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    RestTemplate noRedirectRestTemplate() {
        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplate(requestFactory);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
