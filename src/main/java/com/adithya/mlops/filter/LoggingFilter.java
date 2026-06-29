package com.adithya.mlops.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("--- Incoming Request ---");
        System.out.println("Method: " + requestContext.getMethod());
        System.out.println("URI: " + requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        System.out.println("--- Outgoing Response ---");
        System.out.println("Status: " + responseContext.getStatus());
        System.out.println("Headers: " + responseContext.getStringHeaders());
        System.out.println("-------------------------");
    }
}
