package com.blender.hub.computehub.application.services;

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
//        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
//            @Override
//            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
//                super.prepareConnection(connection, httpMethod);
//                connection.setInstanceFollowRedirects(false);
//            }
//        };
        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplate(requestFactory);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
