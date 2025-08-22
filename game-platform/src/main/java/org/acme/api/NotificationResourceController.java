package org.acme.api;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.acme.service.GamingPlatformService;

@Slf4j
@Path("/api/v1/games/notify")
@RequiredArgsConstructor
public class NotificationResourceController {

    private final GamingPlatformService gamingPlatformService;

    @POST
    public Response notifyNewGame(NewGameNotification notification) {
        if ("platform_a".equals(notification.getPlatformId())) {
            gamingPlatformService.addGame(notification.getGameId());
            return Response.ok("Notification received. Game '" + notification.getGameId() + "' added.").build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Forbidden.").build();
    }

    @Getter
    @Setter
    public static class NewGameNotification {
        private Integer gameId;
        private String platformId;
    }
}
