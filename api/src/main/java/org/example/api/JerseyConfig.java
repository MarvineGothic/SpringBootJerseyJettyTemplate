package org.example.api;

import jakarta.ws.rs.ApplicationPath;
import org.example.infrastructure.AbstractResourceConfig;
import org.example.infrastructure.identity.AuthenticationFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * REST Controllers with Jersey JAX-RS
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends AbstractResourceConfig {

    public JerseyConfig(@Value("${api.resource.path}") String apiResourcePath) {
        super(apiResourcePath);

        // Authentication & Authorization
        register(AuthenticationFilter.class);
        register(RolesAllowedDynamicFeature.class);
    }
}
