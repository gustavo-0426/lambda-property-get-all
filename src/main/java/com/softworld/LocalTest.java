package com.softworld;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.softworld.hexagonal.infrastructure.adapter.input.lambda.PropertyLambdaInputAdapter;

/**
 * Clase para probar el Lambda localmente sin necesidad de AWS
 * 
 * IMPORTANTE: Para pruebas locales con DynamoDB:
 * 1. Instalar AWS CLI y configurar credenciales: aws configure
 * 2. Tener una tabla DynamoDB creada en AWS con el nombre especificado
 * 3. Configurar variables de entorno en pom.xml (exec-maven-plugin)
 * 
 * O usar DynamoDB Local:
 * docker run -p 8000:8000 amazon/dynamodb-local
 */
public class LocalTest {
    
    public static void main(String[] args) {
        System.out.println("=== Lambda Local Test - DynamoDB ===");
        System.out.println("AWS_REGION: " + System.getenv("AWS_REGION"));
        System.out.println("DYNAMODB_TABLE_NAME: " + System.getenv("DYNAMODB_TABLE_NAME"));
        System.out.println();
        
        // Crear una instancia del handler
        PropertyLambdaInputAdapter handler = new PropertyLambdaInputAdapter();
        
        // Crear un evento de prueba (simulando API Gateway)
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/properties");
        
        // Crear un contexto mock
        Context context = new MockContext();
        
        // Ejecutar el handler
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        
        // Mostrar resultado
        System.out.println("\n=== Response ===");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
    }
    
    /**
     * Implementación mock del Context de Lambda para pruebas locales
     */
    static class MockContext implements Context {
        @Override
        public String getAwsRequestId() {
            return "local-test-request-id";
        }
        
        @Override
        public String getLogGroupName() {
            return "/aws/lambda/local-test";
        }
        
        @Override
        public String getLogStreamName() {
            return "local-test-stream";
        }
        
        @Override
        public String getFunctionName() {
            return "PropertyGetAllHandler";
        }
        
        @Override
        public String getFunctionVersion() {
            return "1.0.0";
        }
        
        @Override
        public String getInvokedFunctionArn() {
            return "arn:aws:lambda:local:123456789012:function:PropertyGetAllHandler";
        }
        
        @Override
        public CognitoIdentity getIdentity() {
            return null;
        }
        
        @Override
        public ClientContext getClientContext() {
            return null;
        }
        
        @Override
        public int getRemainingTimeInMillis() {
            return 300000;
        }
        
        @Override
        public int getMemoryLimitInMB() {
            return 512;
        }
        
        @Override
        public LambdaLogger getLogger() {
            return new LambdaLogger() {
                @Override
                public void log(String message) {
                    System.out.println("[LAMBDA] " + message);
                }
                
                @Override
                public void log(byte[] message) {
                    System.out.println("[LAMBDA] " + new String(message));
                }
            };
        }
    }
}
