package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@ApplicationScoped
public class GamingPlatformService {
    private final Set<Integer> allowedGames = ConcurrentHashMap.newKeySet();

    public void addGame(Integer gameId) {
        allowedGames.add(gameId);
        log.info("Gaming Platform: Dynamically added new game: {}", gameId);
    }
}
