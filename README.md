<br>

# 🚀 lambda-property-get-all

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![AWS Lambda](https://img.shields.io/badge/AWS-Lambda-orange.svg)](https://aws.amazon.com/lambda/)
[![DynamoDB](https://img.shields.io/badge/AWS-DynamoDB-blue.svg)](https://aws.amazon.com/dynamodb/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**AWS Lambda Function** serverless para obtener todas las propiedades desde Amazon DynamoDB. Implementa **Arquitectura Hexagonal** (Ports & Adapters) con Java 17, optimizada para despliegue en AWS Lambda con API Gateway.

## 📋 Tabla de Contenidos

- [🚀 Características](#características)
- [📋 Requisitos Previos](#requisitos-previos)
- [⚡ Inicio Rápido](#inicio-rapido)
- [🏗️ Arquitectura](#arquitectura)
- [🔧 Configuración](#configuracion)
- [📦 Despliegue](#despliegue)
- [📚 API Endpoint](#api-endpoint)
- [📞 Contacto](#contacto)

---
<br>

## <a id="características"></a>🚀 Características

- ✅ **AWS Lambda Function** con Java 17
- 🔌 **API Gateway Integration** para endpoints REST
- 🗄️ **Amazon DynamoDB** para almacenamiento serverless (NoSQL)
- 🏗️ **Arquitectura Hexagonal** (Ports & Adapters Pattern)
- 📦 **Maven Shade Plugin** para crear JAR deployable
- 🔒 **Variables de entorno** para configuración segura
- ⚡ **AWS SDK v2** con cliente DynamoDB optimizado
- 🎯 **Mapeo null-safe** con validaciones defensivas
- 💰 **Costo optimizado** con DynamoDB on-demand pricing

---
## <a id="requisitos-previos"></a>📋 Requisitos Previos

- **Java 17**
- **Maven 3.8+**
- **AWS CLI** configurado
- **Cuenta AWS** con permisos para Lambda, API Gateway y DynamoDB
- **Tabla DynamoDB** `properties` creada en AWS
- **Cuenta AWS** con permisos para Lambda, API Gateway y RDS
- **Base de datos MySQL** (RDS o accesible desde Lambda)

---
<br>

## <a id="inicio-rapido"></a>⚡ Inicio Rápido

### 1️⃣ Compilar el Proyecto

```bash
mvn clean package
```

Este comando genera un JAR en `target/lambda-property-get-all-1.0.0.jar` listo para desplegar.

### 2️⃣ Crear Tabla en DynamoDB

**Opción A - AWS Console:**
1. Ve a DynamoDB Console
2. Click en "Create table"
3. **Table name:** `properties`
4. **Partition key:** `id` (Type: **Number**)
5. **Capacity mode:** On-demand
6. Click "Create table"

**Opción B - AWS CLI:**
```bash
aws dynamodb create-table \
  --table-name properties \
  --attribute-definitions AttributeName=id,AttributeType=N \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --region us-east-1
```
## <a id="arquitectura"></a>🏗️ Arquitectura Hexagonal

### Estructura del Proyecto

```
hexagonal/
├── domain/                                    # Capa de Dominio (Core)
│   ├── model/
│   │   └── Property.java                     # Entidad del dominio
│   └── port/
│       ├── input/
│       │   └── GetAllPropertiesInputPort.java    # Puerto de entrada (use case)
│       └── output/
│           └── PropertyRepositoryOutputPort.java # Puerto de salida (abstracción)
│
├── application/                               # Capa de Aplicación
│   └── service/
│       └── GetAllPropertiesService.java      # Implementación del caso de uso
│
└── infrastructure/                            # Capa de Infraestructura
    ├── adapter/
    │   ├── input/                            # Adaptadores primarios (driving)
    │   │   └── lambda/
    │   │       └── PropertyLambdaInputAdapter.java  # AWS Lambda Handler
    │   └── output/                           # Adaptadores secundarios (driven)
    │       └── persistence/
    │           └── dynamodb/
    │               ├── DynamoDBPropertyRepositoryAdapter.java
    │               └── mapper/
    │                   └── PropertyDynamoDBMapper.java
    └── configuration/
        └── DynamoDBConfiguration.java        # Configuración del cliente DynamoDB
```

### Flujo de Ejecución

1. **API Gateway** recibe request HTTP GET `/properties`
2. **PropertyLambdaInputAdapter** (Input Adapter) procesa el evento
3. Invoca **GetAllPropertiesService** (Application Layer) vía puerto de entrada
4. El servicio usa **PropertyRepositoryOutputPort** (abstracción)
5. **DynamoDBPropertyRepositoryAdapter** (Output Adapter) ejecuta Scan en DynamoDB
6. **PropertyDynamoDBMapper** convierte AttributeValue → Property (dominio)
### Variables de Entorno en AWS Lambda

Configurar las siguientes variables de entorno en la consola de Lambda:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `AWS_REGION` | Región de AWS donde está DynamoDB | `us-east-1` |
| `DYNAMODB_TABLE_NAME` | Nombre de la tabla DynamoDB | `properties` |

**Nota:** Las credenciales de AWS se obtienen automáticamente del rol de ejecución de Lambda.

---
<br>

## <a id="arquitectura"></a>🏗️ Arquitectura
### Opción 1: AWS Console

1. **Compilar el proyecto:** `mvn clean package`
2. Ir a **AWS Lambda Console**
3. Click en **"Create function"**
4. Seleccionar **Java 17 runtime**
5. Subir `target/lambda-property-get-all-1.0.0.jar`
6. Configurar **Handler:** `com.softworld.hexagonal.infrastructure.adapter.input.lambda.PropertyLambdaInputAdapter`
7. Agregar variables de entorno:
   - `AWS_REGION`: `us-east-1`
   - `DYNAMODB_TABLE_NAME`: `properties`
8. Agregar política IAM: **AmazonDynamoDBReadOnlyAccess**
9. Configurar **API Gateway trigger**
1. API Gateway recibe request HTTP GET
2. Lambda invoca `PropertyGetAllHandler.handleRequest()`
3. Se establece conexión a MySQL usando variables de entorno
4. Se ejecuta query `SELECT * FROM property`
5. Resultados se serializan a JSON con Gson
6. API Gateway retorna respuesta al cliente

---
<br>

## <a id="configuracion"></a>🔧 Configuración

### Variables de Entorno en AWS Lambda

Configurar las siguientes variables de entorno en la consola de Lambda:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL de conexión MySQL | `mydb.123456.us-east-1.rds.amazonaws.com:3306/properties_db` |
| `DB_USER` | Usuario de base de datos | `admin` |
| `DB_PASSWORD` | Contraseña de base de datos | `your-secure-password` |

---
<br>

## <a id="despliegue"></a>📦 Despliegue

### Opción 1: AWS Console

1. Compilar el proyecto: `mvn clean package`
2. Ir a AWS Lambda Console
3. Crear nueva función con Java 17 runtime
### Opción 2: AWS CLI

```bash
# Crear función Lambda
aws lambda create-function \
  --function-name property-get-all \
  --runtime java17 \
  --handler com.softworld.hexagonal.infrastructure.adapter.input.lambda.PropertyLambdaInputAdapter \
  --zip-file fileb://target/lambda-property-get-all-1.0.0.jar \
  --role arn:aws:iam::YOUR_ACCOUNT:role/lambda-execution-role \
  --environment Variables="{AWS_REGION=us-east-1,DYNAMODB_TABLE_NAME=properties}" \
  --timeout 30 \
  --memory-size 512

# Agregar permisos de DynamoDB al rol
aws iam attach-role-policy \
  --role-name lambda-execution-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonDynamoDBReadOnlyAccess

# Actualizar función existente
aws lambda update-function-code \
  --function-name property-get-all \
  --zip-file fileb://target/lambda-property-get-all-1.0.0.jar
```
# Actualizar función existente
aws lambda update-function-code \
  --function-name property-get-all \
  --zip-file fileb://target/lambda-property-get-all-1.0.0.jar
```

---
<br>

## <a id="api-endpoint"></a>📚 API Endpoint

### GET /properties

Obtiene todas las propiedades disponibles en la base de datos.

**Request:**
```http
GET /properties HTTP/1.1
Host: your-api-gateway-url.amazonaws.com
```

**Response Success (200):**
```json
[
  {
    "id": 1,
    "ownerId": 101,
    "name": "Casa en la playa",
    "description": "Hermosa casa frente al mar",
    "propertyType": "Casa",
    "price": 250000.00,
    "available": true
  },
  {
**Response Error (500):**
```json
{
  "error": "Internal Server Error",
  "message": "Error al obtener propiedades desde DynamoDB: ..."
}
```

---
<br>

## 🧪 Testing

### Test Event en Lambda Console

```json
{
  "httpMethod": "GET",
  "path": "/properties"
}
```

### Probar con curl

```bash
curl -X GET https://your-api-id.execute-api.us-east-1.amazonaws.com/prod/properties
``` "available": true
  }
]
```

**Response Error (500):**
```json
{
  "error": "Internal Server Error",
  "message": "Failed connection to database: ..."
}
```

---
<br>

## <a id="contacto"></a>📞 Contacto 


### Gustavo Castro

**Ingeniero de Sistemas**  
**Especialista en Ingeniería de Software**  
**Desarrollador Backend Senior, Spring Boot, Node.js, Arquitectura Cloud (AWS)**  
**GitHub:** [github.com/gustavo-0426](https://github.com/gustavo-0426)  
**LinkedIn:** [linkedin.com/in/gustavo-castro-prasca](https://linkedin.com/in/gustavo-castro-prasca)

---
