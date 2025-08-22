package org.acme.api;

import jakarta.json.JsonNumber;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Path("/games/play")
@RequiredArgsConstructor
public class GameResourceController {

    private final JsonWebToken jwt;

    @GET
    @Path("/{gameId}")
    public Response play(@PathParam("gameId") Integer gameId) {
        try {
            // Authorization check: Game Provider A extracts the payload
            String platformId = jwt.getClaim("platform_id");
            String userId = jwt.getSubject();

            List<?> rawAllowedGames = jwt.getClaim("game_access");

            // Convert List<?> to Set<Integer> for efficient lookup and correct types
            Set<Integer> allowedGameIds = new HashSet<>();
            if (rawAllowedGames != null) {
                for (Object gameObj : rawAllowedGames) {
                    if (gameObj instanceof JsonNumber) {
                        // Extract int value from JsonNumber
                        allowedGameIds.add(((JsonNumber) gameObj).intValue());
                    } else if (gameObj instanceof Number) {
                        // Fallback if it was already a regular Number
                        allowedGameIds.add(((Number) gameObj).intValue());
                    } else {
                        log.warn("Unexpected type found in game_access claim for user {}: {}." +
                                " Expected JsonNumber or Number.", userId, gameObj.getClass().getName());
                    }
                }
            }

            if (allowedGameIds.contains(gameId)) {
                // Use case 1: Authenticated access granted
                // Log successful access at INFO level
                log.info("User {} from platform {} successfully accessed game {}.", userId, platformId, gameId);
                return Response.ok("Welcome, " + userId +
                        "! You are now playing " + gameId + " from platform " + platformId).build();
            } else {
                // Use case 3: Game not allowed for the platform/user
                // Log denied access at WARN level
                log.warn("Access Denied: User {} from platform {} attempted to access unauthorized game {}.",
                        userId, platformId, gameId);
                return Response.status(Response.Status.FORBIDDEN).entity("Access Denied: Game " + gameId +
                        " is not allowed for your platform/user.").build();
            }
        } catch (Exception e) {
            // Use case 2: Unauthorized or invalid call
            // Log the full exception details at ERROR level for debugging
            log.error("Error during game access for game ID {}: {}. User: {}, Platform: {}.",
                    gameId, e.getMessage(), jwt.getSubject(), jwt.getClaim("platform_id"), e);
            // Return a generic unauthorized message to the client for security
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized or invalid request.").build();
        }
    }
}
