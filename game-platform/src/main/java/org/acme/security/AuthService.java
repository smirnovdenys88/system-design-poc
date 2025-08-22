package org.acme.security;


import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.acme.api.request.UserCredential;
import org.acme.service.GamingPlatformService;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final GamingPlatformService gamingPlatformService;

    public String loginAndGenerateJwt(UserCredential credential) {
        long currentTimestamp = Instant.now().getEpochSecond();
        if ("password".equals(credential.password)) {
            Set<Integer> allowedGames = gamingPlatformService.getAllowedGames();
            return Jwt.claims()
                    .subject(credential.name)
                    .claim("platform_id", "platform_a")
                    .claim("game_access", allowedGames)
                    .issuer("https://acme.org")
                    .issuedAt(currentTimestamp) // one hour
                    .sign();
        }
        return null;
    }
}
