package com.adithya.mlops.resource;

import com.adithya.mlops.exception.ResourceNotFoundException;
import com.adithya.mlops.exception.WorkspaceNotEmptyException;
import com.adithya.mlops.model.MLWorkspace;
import com.adithya.mlops.repository.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/workspaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkspaceResource {

    private DataStore dataStore = DataStore.getInstance();

    @GET
    public List<MLWorkspace> getAllWorkspaces() {
        return new ArrayList<>(dataStore.getWorkspaces().values());
    }

    @POST
    public Response createWorkspace(MLWorkspace workspace) {
        if (workspace.getId() == null || workspace.getId().isEmpty()) {
            workspace.setId(UUID.randomUUID().toString());
        }
        if (workspace.getModelIds() == null) {
            workspace.setModelIds(new ArrayList<>());
        }
        dataStore.getWorkspaces().put(workspace.getId(), workspace);
        return Response.status(Response.Status.CREATED).entity(workspace).build();
    }

    @GET
    @Path("/{workspaceId}")
    public MLWorkspace getWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = dataStore.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace with ID " + workspaceId + " not found");
        }
        return workspace;
    }

    @DELETE
    @Path("/{workspaceId}")
    public Response deleteWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = dataStore.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            throw new ResourceNotFoundException("Workspace with ID " + workspaceId + " not found");
        }
        
        if (workspace.getModelIds() != null && !workspace.getModelIds().isEmpty()) {
            throw new WorkspaceNotEmptyException("Cannot delete workspace. Models exist.");
        }
        
        dataStore.getWorkspaces().remove(workspaceId);
        return Response.noContent().build();
    }
}
