package com.blender.hub.computehub;

import com.blender.hub.computehub.usecase.manager.port.driven.ManagerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class FlamencoManagerLinkingIT extends AbstractBaseIT {
    @SpyBean
    ManagerRepo managerRepo;

    @Test
    public void createManagerTest() {
        given()
                .body(Map.of("managerType", "flamenco"))
            .when()
                .post("/managers")
            .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value());
    }
}
