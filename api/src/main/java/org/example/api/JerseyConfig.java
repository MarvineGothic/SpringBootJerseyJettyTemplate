package org.example.api;

import jakarta.ws.rs.ApplicationPath;
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
    }
}
