package com.blender.hub.computehub;

import com.blender.hub.computehub.usecase.manager.entity.FlamencoManager;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerRepo;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

@Slf4j
public class FlamencoManagerLinkingIT extends AbstractBaseIT {
    @SpyBean
    ManagerRepo managerRepo;

    @Test
    public void createManagerTest() {
        ExtractableResponse<Response> response = requestNewFlamencoManager()
                .header("Location", not(blankOrNullString()))
                .extract();

        List<String> pathSegments = UriComponentsBuilder.fromUriString(response.header("Location")).build()
                .getPathSegments();
        String managerId = pathSegments.get(pathSegments.size()-1);
        log.info("extracted managerId: {}", managerId);
        Optional<FlamencoManager> storedManager = managerRepo.get(managerId);
        assertThat(storedManager.isPresent()).isTrue();
    }
}
