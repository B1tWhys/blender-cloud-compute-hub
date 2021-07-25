package com.blender.hub.computehub;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractBaseIT {
    @LocalServerPort
    int localServerPort;

    @BeforeEach
    void setupRestAssured() {
        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.basePath = "/api";
    }
}
