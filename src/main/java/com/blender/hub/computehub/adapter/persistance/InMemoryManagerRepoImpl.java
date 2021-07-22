package com.blender.hub.computehub.adapter.persistance;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryManagerRepoImpl implements ManagerRepo {
    private final static ConcurrentMap<String, Manager> managerMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Manager> get(String id) {
        return Optional.ofNullable(managerMap.get(id));
    }

    @Override
    public Optional<Manager> getByHmacId(String hmacId) {
        return managerMap.values().stream()
                .filter(m -> Optional.of(m)
                        .map(Manager::getHmacSecret)
                        .map(HmacSecret::getId)
                        .map(i -> i.equals(hmacId))
                        .orElse(false))
                .findFirst();
    }

    @Override
    public List<Manager> getMostRecentlyCreated(int limit) {
        return managerMap.entrySet().stream().
                sorted(Comparator.comparing(e -> e.getValue().getCreatedTs()))
                .limit(limit)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void upsert(Manager manager) {
        managerMap.put(manager.getId(), manager);
    }
}
