package org.acme.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.api.request.UserCredential;
import org.acme.api.response.UserToken;
import org.acme.security.AuthService;

@Path("/auth")
public class AuthController {
    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredential credential) {
        String jwt = authService.loginAndGenerateJwt(credential);
        if (jwt != null) {
            return Response.ok(new UserToken(jwt)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials.").build();
    }
}
