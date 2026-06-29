package com.adithya.mlops.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BaseResource {

    @GET
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("apiName", "MLOps Pipeline Management API");
        response.put("version", "v1");
        response.put("adminContact", "adithyaabhiman2006@gmail.com");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("workspaces", "/api/v1/workspaces");
        resources.put("models", "/api/v1/models");

        response.put("resources", resources);
        return response;
    }
}