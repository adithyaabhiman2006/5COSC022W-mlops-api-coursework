package com.adithya.mlops.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class BaseResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getApiStatus() {
        return "{\"status\":\"API is up and running\", \"version\":\"v1\"}";
    }
}
