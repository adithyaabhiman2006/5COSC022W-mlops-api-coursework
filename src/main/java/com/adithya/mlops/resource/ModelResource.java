package com.adithya.mlops.resource;

import com.adithya.mlops.exception.LinkedWorkspaceNotFoundException;
import com.adithya.mlops.exception.ResourceNotFoundException;
import com.adithya.mlops.model.MLWorkspace;
import com.adithya.mlops.model.MachineLearningModel;
import com.adithya.mlops.repository.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/models")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {

    private DataStore dataStore = DataStore.getInstance();

    @GET
    public List<MachineLearningModel> getModels(@QueryParam("status") String status) {
        List<MachineLearningModel> models = new ArrayList<>(dataStore.getModels().values());
        if (status != null && !status.isEmpty()) {
            return models.stream()
                    .filter(m -> status.equals(m.getStatus()))
                    .collect(Collectors.toList());
        }
        return models;
    }

    @POST
    public Response createModel(MachineLearningModel model) {
        if (model.getWorkspaceId() == null || !dataStore.getWorkspaces().containsKey(model.getWorkspaceId())) {
            throw new LinkedWorkspaceNotFoundException("Invalid workspaceId: " + model.getWorkspaceId());
        }

        if (model.getId() == null || model.getId().isEmpty()) {
            model.setId(UUID.randomUUID().toString());
        }

        if (model.getStatus() == null || model.getStatus().isEmpty()) {
            model.setStatus("TRAINING");
        }

        dataStore.getModels().put(model.getId(), model);
        
        // Add model to workspace
        MLWorkspace workspace = dataStore.getWorkspaces().get(model.getWorkspaceId());
        workspace.getModelIds().add(model.getId());

        return Response.status(Response.Status.CREATED).entity(model).build();
    }

    // Sub-resource locator for metrics
    @Path("/{modelId}/metrics")
    public EvaluationMetricResource getMetricsResource(@PathParam("modelId") String modelId) {
        if (!dataStore.getModels().containsKey(modelId)) {
            throw new ResourceNotFoundException("Model with ID " + modelId + " not found");
        }
        return new EvaluationMetricResource(modelId);
    }
}
