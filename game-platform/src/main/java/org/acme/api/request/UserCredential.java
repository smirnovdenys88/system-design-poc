package org.acme.api.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@RegisterForReflection
@Data
@Builder(toBuilder = true)
public class UserCredential {
    public String name;
    public String password;
}
