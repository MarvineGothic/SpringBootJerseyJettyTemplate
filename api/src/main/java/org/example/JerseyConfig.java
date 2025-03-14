package org.example;

import jakarta.ws.rs.ApplicationPath;
import org.example.api.UserResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * REST Controllers with Jersey JAX-RS
*/

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        setProperties(Collections.singletonMap("jersey.config.server.response.setStatusOverSendError", true));
        property(ServletProperties.FILTER_FORWARD_ON_404, true); // to allow SpringMVC to handle
        register(JacksonFeature.class);

        register(UserResource.class);
    }
}
