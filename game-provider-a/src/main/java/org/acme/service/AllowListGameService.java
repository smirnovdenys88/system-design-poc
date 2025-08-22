package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.config.AllowListGameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AllowListGameService {
    private final AllowListGameConfig allowListGameConfig;
    private final NotificationService notificationService;

    public Map<String, List<Integer>> getSharedGames() {
        return allowListGameConfig.sharedGames();
    }

    public void shareGameWithPlatform(Integer gameId, String platformId) {
        log.info("Share Game with Platform ID: " + platformId + " with Game ID: " + gameId);
        allowListGameConfig.sharedGames().computeIfAbsent(platformId, k -> new ArrayList<>()).add(gameId);
        notificationService.addNewGameNotification(gameId, platformId);
    }
}
