package org.example;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import jakarta.ws.rs.ApplicationPath;
import org.example.model.error.ServiceExceptionMapper;
import org.example.model.error.ValidationExceptionMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * REST Controllers with Jersey JAX-RS
 */

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/appendix-properties.html
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        register(RequestContextFilter.class);

        register(ServiceExceptionMapper.class); // register custom exception mapper
        register(ValidationExceptionMapper.class); // register validation exception mapper
        register(JacksonFeature.class);

        // register resources
        register(UserResource.class);
    }
}
