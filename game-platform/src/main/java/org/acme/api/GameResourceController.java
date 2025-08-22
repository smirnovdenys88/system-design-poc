package org.acme.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.acme.service.GamingPlatformService;

import java.net.URI;

@Path("/games")
@RequiredArgsConstructor
public class GameResourceController {
    private final GamingPlatformService gamingPlatformService;

    @GET
    public Response getGames() {
        return Response.ok(gamingPlatformService.getAllowedGames()).build();
    }

    @GET
    @Path("/play/{gameId}")
    public Response playGame(@PathParam("gameId") String gameId, @HeaderParam("Authorization") String jwt) {
        URI gameUrl = URI.create("http://localhost:8081/games/play/" + gameId + "?token=" + jwt);
        return Response.seeOther(gameUrl).build();
    }
}
