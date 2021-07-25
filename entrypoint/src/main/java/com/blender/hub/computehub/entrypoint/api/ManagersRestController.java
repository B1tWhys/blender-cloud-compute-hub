package com.blender.hub.computehub.entrypoint.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController("/api/managers")
@Slf4j
@AllArgsConstructor
public class ManagersRestController {
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getManagers() {
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
