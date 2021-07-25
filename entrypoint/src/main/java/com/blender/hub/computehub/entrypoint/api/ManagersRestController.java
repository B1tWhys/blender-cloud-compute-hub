package com.blender.hub.computehub.entrypoint.api;

import com.blender.hub.computehub.entity.manager.CreateManagerCommand;
import com.blender.hub.computehub.entity.manager.Manager;
import com.blender.hub.computehub.entity.manager.ManagerType;
import com.blender.hub.computehub.usecase.manager.port.driving.CreateManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/managers")
public class ManagersRestController {
    CreateManager createManager;
    ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getManagers() {
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{managerId}")
    public ResponseEntity<Map<String, Object>> getManager(@PathVariable("managerId") String managerId) {
        log.debug("get manager id: {}", managerId);
        return ResponseEntity.ok().build(); // TODO
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createManager(@RequestBody CreateManagerRequest request) throws JsonProcessingException {
        log.debug("create manager request: {}", objectMapper.writeValueAsString(request));
        Manager createdManager = createManager.createManager(CreateManagerCommand.builder()
                .managerType(ManagerType.valueOf(request.managerType.toUpperCase(Locale.ROOT)))
                .build());
        String id = createdManager.getId();

        return ResponseEntity.created(linkTo(methodOn(ManagersRestController.class).getManager(id)).toUri()).build();
    }
}
