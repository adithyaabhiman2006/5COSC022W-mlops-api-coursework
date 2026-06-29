package com.adithya.mlops.resource;

import com.adithya.mlops.exception.ModelDeprecatedException;
import com.adithya.mlops.model.EvaluationMetric;
import com.adithya.mlops.model.MachineLearningModel;
import com.adithya.mlops.repository.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationMetricResource {

    private String modelId;
    private DataStore dataStore = DataStore.getInstance();

    public EvaluationMetricResource(String modelId) {
        this.modelId = modelId;
    }

    @GET
    public List<EvaluationMetric> getMetrics() {
        return dataStore.getMetrics().getOrDefault(modelId, new ArrayList<>());
    }

    @POST
    public Response addMetric(EvaluationMetric metric) {
        MachineLearningModel parentModel = dataStore.getModels().get(modelId);
        
        if ("DEPRECATED".equals(parentModel.getStatus())) {
            throw new ModelDeprecatedException("Cannot add metric to DEPRECATED model.");
        }

        if (metric.getId() == null || metric.getId().isEmpty()) {
            metric.setId(UUID.randomUUID().toString());
        }
        if (metric.getTimestamp() == 0) {
            metric.setTimestamp(System.currentTimeMillis());
        }

        // Add metric to data store
        dataStore.getMetrics().computeIfAbsent(modelId, k -> new ArrayList<>()).add(metric);

        // Update parent model latestAccuracy
        parentModel.setLatestAccuracy(metric.getAccuracyScore());

        return Response.status(Response.Status.CREATED).entity(metric).build();
    }
}
