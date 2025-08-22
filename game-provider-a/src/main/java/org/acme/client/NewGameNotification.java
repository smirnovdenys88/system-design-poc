package org.acme.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewGameNotification {
    private Integer gameId;
    private String platformId;
}
