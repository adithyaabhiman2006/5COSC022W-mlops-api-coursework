package com.adithya.mlops.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Scan the packages for resources, exceptions and filters
        packages("com.adithya.mlops.resource", 
                 "com.adithya.mlops.exception",
                 "com.adithya.mlops.filter");
    }
}
