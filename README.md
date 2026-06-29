# MLOps Pipeline Management API

## API Overview
This is a JAX-RS (Jersey) REST API for managing an MLOps Pipeline. The API provides endpoints to manage `MLWorkspace`, `MachineLearningModel`, and `EvaluationMetric` entities. It runs on an embedded Grizzly server without Spring Boot or a database (using in-memory data structures).

### Base Path
All endpoints are relative to: `http://localhost:8080/api/v1`

## Build and Run Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.x

### Build the Project
Open a terminal in the project directory and run:
```bash
mvn clean compile
```

### Run the Application
Start the embedded Grizzly server using the Maven Exec plugin:
```bash
mvn exec:java
```
The server will start on `http://localhost:8080/api/v1`. To stop the server, press `Ctrl-C` in the terminal.

## Curl Commands

1. **Check API Status**
```bash
curl -X GET http://localhost:8080/api/v1
```

2. **Create a Workspace**
```bash
curl -X POST http://localhost:8080/api/v1/workspaces \
-H "Content-Type: application/json" \
-d '{"teamName":"DataScience-Alpha", "storageQuotaGb":100}'
```

3. **Get All Workspaces**
```bash
curl -X GET http://localhost:8080/api/v1/workspaces
```

4. **Create a Model** (Replace `<workspace_id>` with an actual ID from the previous step)
```bash
curl -X POST http://localhost:8080/api/v1/models \
-H "Content-Type: application/json" \
-d '{"framework":"TensorFlow", "status":"TRAINING", "workspaceId":"<workspace_id>"}'
```

5. **Get Deployed Models**
```bash
curl -X GET "http://localhost:8080/api/v1/models?status=DEPLOYED"
```

6. **Add an Evaluation Metric** (Replace `<model_id>` with an actual Model ID)
```bash
curl -X POST http://localhost:8080/api/v1/models/<model_id>/metrics \
-H "Content-Type: application/json" \
-d '{"accuracyScore": 0.95}'
```

7. **Delete a Workspace** (Will fail with 409 if the workspace contains models)
```bash
curl -X DELETE http://localhost:8080/api/v1/workspaces/<workspace_id>
```

## Conceptual Answers

1. **Why do we use @ApplicationPath?**
   `@ApplicationPath` is used in JAX-RS to define the base URI for all resources hosted within the application. It acts as a root path (e.g., `/api/v1`), meaning that all specific resource `@Path` annotations will be appended to this base path, allowing for clean API versioning and organization.

2. **How does the Sub-Resource Locator pattern work in JAX-RS?**
   The Sub-Resource Locator pattern is implemented by annotating a method with `@Path` but omitting HTTP method annotations (like `@GET` or `@POST`). When a request matches this path, JAX-RS delegates the request to the object returned by the method. In our case, `ModelResource` delegates requests for `/{modelId}/metrics` to an instance of `EvaluationMetricResource`, which then handles the HTTP methods for that specific model.

3. **Why do we need ExceptionMapper?**
   `ExceptionMapper` allows us to intercept exceptions thrown during request processing and translate them into appropriate HTTP responses. Instead of returning a generic 500 error or a stack trace to the client, we can catch custom exceptions (like `WorkspaceNotEmptyException`) and map them to a standardized JSON response with the correct HTTP status code (e.g., 409 Conflict), providing a better and safer client experience.

4. **What is the purpose of ContainerRequestFilter and ContainerResponseFilter?**
   These filters provide a way to intercept and process incoming HTTP requests and outgoing HTTP responses globally. `ContainerRequestFilter` can be used for tasks like authentication, logging incoming URLs, or modifying headers before the request reaches the resource method. `ContainerResponseFilter` is used for tasks like adding CORS headers or logging response status codes before the response is sent back to the client.
