package org.acme.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "provider.shared.game")
public interface AllowListGameConfig {
    Map<String, List<Integer>> sharedGames();
}
