package org.acme.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.service.AllowListGameService;

@Slf4j
@Path("/games/provide")
@RequiredArgsConstructor
public class GameProvideController {

    private final AllowListGameService allowListGameService;

    @GET
    public Response getGames() {
        return Response.ok(allowListGameService.getSharedGames()).build();
    }

    @POST
    public Response shareGameWithPlatform(ShareGame game) {
        allowListGameService.shareGameWithPlatform(game.id, game.platformId);
        return Response.ok().build();
    }

    public record ShareGame(Integer id, String platformId) {}
}
