package com.blender.hub.computehub;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;

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

    protected ValidatableResponse requestNewFlamencoManager() {
        return given()
                .body(Map.of("managerType", "flamenco"))
                .contentType(ContentType.JSON)
                .header(new Header("Host", "localhost:" + localServerPort))
                .when()
                .post("/managers")
                .then()
                .log().headers()
                .statusCode(HttpStatus.CREATED.value());
    }
}
