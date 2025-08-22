package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.client.GamingPlatformClient;
import org.acme.client.NewGameNotification;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class NotificationService {

    @Inject
    @RestClient
    GamingPlatformClient gamingPlatformClient;

    public void addNewGameNotification(Integer gameId, String platformId) {
        NewGameNotification notification = new NewGameNotification();
        notification.setGameId(gameId);
        notification.setPlatformId(platformId);

        try {
            Response response = gamingPlatformClient.notifyNewGame(notification);
            log.info("New Game Notification Response: {}", response);
        } catch (Exception e) {
            log.error("Error while sending New Game Notification Response", e);
        }
    }
}
