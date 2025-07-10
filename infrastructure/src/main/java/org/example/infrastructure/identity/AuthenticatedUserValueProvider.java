package org.example.infrastructure.identity;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.example.authentication.AuthUser;
import org.example.authentication.UserPrincipal;
import org.example.authentication.annotation.AuthenticatedUser;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import java.util.function.Function;

@Provider
@Singleton
public class AuthenticatedUserValueProvider extends AbstractValueParamProvider {

    @Inject
    public AuthenticatedUserValueProvider(jakarta.inject.Provider<MultivaluedParameterExtractorProvider> mpep) {
        super(mpep, Parameter.Source.UNKNOWN);
    }

    @Override
    protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
        if (parameter.getAnnotation(AuthenticatedUser.class) != null &&
                parameter.getRawType().equals(AuthUser.class)) {
            return ctx -> {
                var userPrincipal = (UserPrincipal) ctx.getSecurityContext().getUserPrincipal();
                return userPrincipal != null ? userPrincipal.getAuthUser() : null;
            };
        }
        return null;
    }
}
