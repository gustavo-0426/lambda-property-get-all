package com.softworld.hexagonal.infrastructure.adapter.input.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.softworld.hexagonal.application.service.GetAllPropertiesService;
import com.softworld.hexagonal.domain.model.Property;
import com.softworld.hexagonal.domain.port.input.GetAllPropertiesInputPort;
import com.softworld.hexagonal.infrastructure.adapter.output.persistence.mysql.MySqlPropertyRepositoryAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * INPUT ADAPTER (Adaptador de Entrada Primario)
 * Punto de entrada a la aplicación desde AWS Lambda.
 * - Recibe eventos externos (API Gateway)
 * - Invoca el puerto de ENTRADA (use case)
 * - Convierte respuesta del dominio a formato externo (JSON)
 */
public class PropertyLambdaInputAdapter implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson gson = new Gson();
    private static final String EXPECTED_PATH = "/properties";
    private static final String EXPECTED_METHOD = "GET";

    private final GetAllPropertiesInputPort getAllPropertiesInputPort;

    public PropertyLambdaInputAdapter() {
        MySqlPropertyRepositoryAdapter repositoryAdapter = new MySqlPropertyRepositoryAdapter();
        this.getAllPropertiesInputPort = new GetAllPropertiesService(repositoryAdapter);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        if (!EXPECTED_METHOD.equals(request.getHttpMethod())) {
            return buildErrorResponse(405, "Method Not Allowed", 
                "Only GET method is supported");
        }

        String path = request.getPath();
        if (path != null && !path.equals(EXPECTED_PATH) && !path.endsWith(EXPECTED_PATH)) {
            return buildErrorResponse(404, "Not Found", 
                "Path not found: " + path);
        }

        try {
            List<Property> properties = getAllPropertiesInputPort.getAllProperties();

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(gson.toJson(properties));

        } catch (Exception e) {
            context.getLogger().log("Error executing use case: " + e.getMessage());
            return buildErrorResponse(500, "Internal Server Error", e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String error, String message) {
        Map<String, String> responseError = new HashMap<>();
        responseError.put("error", error);
        responseError.put("message", message);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of(
                        "Content-Type", "application/json",
                        "X-Custom-Header", "PropertyGetAllHandler"
                ))
                .withBody(gson.toJson(responseError));
    }
}
