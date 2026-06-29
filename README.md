# MLOps Pipeline Management API

## API Overview
This API is used for managing a pipeline that combines machine learning and operations. It's built using JAX-RS, also known as Jersey, and provides several endpoints for managing different parts of the pipeline. You can use it to manage your machine learning workspace, models, and metrics for evaluating those models. The API is pretty lightweight, running on a Grizzly server without needing a big framework like Spring Boot or a database - it just uses simple in-memory data structures to store information.

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
To get started, the server will be available at http://localhost:8080/api/v1. If you need to stop it, just press Ctrl-C in your terminal and it will shut down.

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

If you want to get rid of a workspace, you should know that it won't work if there are models in it - you'll just get an error message back.
curl -X DELETE http://localhost:8080/api/v1/workspaces/<workspace_id>
Conceptual Answers
So, why do we need to use @ApplicationPath? Well, it's actually pretty simple. @ApplicationPath is used in JAX-RS to define the base URI for all the resources that are hosted within an application. Think of it like a root path, like /api/v1, that all the specific resource @Path annotations will be appended to. This makes it really easy to keep your API organized and versioned cleanly. For example, if you have @ApplicationPath("/api/v1") and a resource with @Path("/users"), the full path would be /api/v1/users. It's a great way to keep everything tidy and make it easy to manage different versions of your API.

So, you want to know how the Sub-Resource Locator pattern works in JAX-RS. Well, it's actually pretty simple. You see, this pattern is used when you have a resource that needs to delegate requests to another resource. To make it work, you annotate a method with @Path, but you don't use any HTTP method annotations like @GET or @POST. When a request matches the path, JAX-RS sends it to the object that the method returns. For example, let's say you have a resource called ModelResource. It can delegate requests for a certain path, like /{modelId}/metrics, to another resource called EvaluationMetricResource. Then, EvaluationMetricResource handles the HTTP methods for that specific model. It's kind of like a relay team, where the first resource passes the request to the next one, which then takes care of it. This pattern is really useful when you have complex resources that need to be broken down into smaller, more manageable parts. And the best part is, it's not that hard to implement, once you get the hang of it.

We need ExceptionMapper because it helps us catch exceptions that happen when we're processing requests. This way, we can turn these exceptions into proper HTTP responses that make sense to the client. Instead of just sending back a generic error message or a stack trace, we can catch specific exceptions - like WorkspaceNotEmptyException - and turn them into a standardized JSON response with the right HTTP status code, such as 409 Conflict. This makes the experience better and safer for the client. For example, when a WorkspaceNotEmptyException is thrown, we can map it to a 409 Conflict response, which tells the client that the workspace is not empty and cannot be deleted. This approach allows us to handle exceptions in a more controlled and user-friendly way, providing a better experience for the client. By using ExceptionMapper, we can ensure that our application returns meaningful and informative error messages, rather than just generic errors. This helps to improve the overall quality and reliability of our application.

What is the purpose of ContainerRequestFilter and ContainerResponseFilter? These filters provide a way to intercept and process incoming HTTP requests and outgoing HTTP responses globally. ContainerRequestFilter can be used for tasks like authentication, logging incoming URLs, or modifying headers before the request reaches the resource method. ContainerResponseFilter is used for tasks like adding CORS headers or logging response status codes before the response is sent back to the client.
