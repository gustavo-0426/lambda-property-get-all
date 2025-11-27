package com.softworld.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.softworld.model.Property;
import com.softworld.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyGetAllHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson gson = new Gson();
    private static final String EXPECTED_PATH = "/properties";
    private static final String EXPECTED_METHOD = "GET";

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

        List<Property> propertyList = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM property")) {

            while (resultSet.next()) {

                Property property = new Property();

                property.setId(resultSet.getLong("id"));
                property.setOwnerId(resultSet.getLong("owner_id"));
                property.setName(resultSet.getString("name"));
                property.setDescription(resultSet.getString("description"));
                property.setPropertyType(resultSet.getString("property_type"));
                property.setPrice(resultSet.getDouble("price"));
                property.setAvailable(resultSet.getBoolean("available"));

                propertyList.add(property);
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(gson.toJson(propertyList));

        } catch (Exception e) {
            context.getLogger().log("Failed connection to database: " + e.getMessage());
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
