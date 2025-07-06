package org.example.infrastructure;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import org.example.ServerResponseFilter;
import org.example.error.ServiceExceptionMapper;
import org.example.error.ValidationExceptionMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public abstract class AbstractResourceConfig  extends ResourceConfig {

    @Context
    private ServletConfig servletConfig;

    public AbstractResourceConfig(@Value("${api.resource.path}") String apiResourcePath) {
        super();
        // https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/appendix-properties.html
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        register(RequestContextFilter.class);
        register(JacksonFeature.class);

        register(ServerResponseFilter.class);

        register(ServiceExceptionMapper.class); // register custom exception mapper
        register(ValidationExceptionMapper.class); // register validation exception mapper

        // register resources
        packages(apiResourcePath);

        // Swagger
        register(OpenApiResource.class);
        register(AcceptHeaderOpenApiResource.class);
        configureOpenapi(servletConfig, apiResourcePath);
    }

    private void configureOpenapi(@Context ServletConfig servletConfig, String apiResourcePath) {
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("Swagger Sample App bootstrap code")
                .description("This is a sample server Petstore server.  You can find out more about Swagger " +
                        "at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, " +
                        "you can use the api key `special-key` to test the authorization filters.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .email("apiteam@swagger.io"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        oas.info(info);

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Set.of(apiResourcePath));

        try {
            new JaxrsOpenApiContextBuilder<>()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
