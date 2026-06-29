package com.adithya.mlops.repository;

import com.adithya.mlops.model.EvaluationMetric;
import com.adithya.mlops.model.MLWorkspace;
import com.adithya.mlops.model.MachineLearningModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private static DataStore instance;
    private Map<String, MLWorkspace> workspaces;
    private Map<String, MachineLearningModel> models;
    // Map of Model ID -> List of EvaluationMetrics
    private Map<String, List<EvaluationMetric>> metrics;

    private DataStore() {
        workspaces = new HashMap<>();
        models = new HashMap<>();
        metrics = new HashMap<>();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public Map<String, MLWorkspace> getWorkspaces() { return workspaces; }
    public Map<String, MachineLearningModel> getModels() { return models; }
    public Map<String, List<EvaluationMetric>> getMetrics() { return metrics; }
}
