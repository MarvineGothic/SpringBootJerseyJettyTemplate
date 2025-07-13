package org.example;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext reqCtx, ContainerResponseContext resCtx) {
        resCtx.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        resCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
        resCtx.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        resCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
