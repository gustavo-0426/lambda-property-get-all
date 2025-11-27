<br>

# 🚀 lambda-property-get-all

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![AWS Lambda](https://img.shields.io/badge/AWS-Lambda-orange.svg)](https://aws.amazon.com/lambda/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**AWS Lambda Function** para obtener todas las propiedades desde una base de datos MySQL. Función serverless lista para desplegar en AWS Lambda con API Gateway.

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
- 💾 **Conexión MySQL** para gestión de propiedades
- 📦 **Maven Shade Plugin** para crear JAR deployable
- 🎯 **Lombok** para reducir código boilerplate
- 🔒 **Variables de entorno** para configuración segura
- ⚡ **RequestHandler** optimizado para respuestas rápidas

---
<br>

## <a id="requisitos-previos"></a>📋 Requisitos Previos

- **Java 17**
- **Maven 3.8+**
- **AWS CLI** configurado
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

### 2️⃣ Estructura de la Base de Datos

Ejecuta el script SQL para crear la tabla de propiedades:

```sql
CREATE TABLE property (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    property_type VARCHAR(100),
    price DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE
);
```

---
<br>

## <a id="arquitectura"></a>🏗️ Arquitectura

### Componentes Principales

- **PropertyGetAllHandler**: Handler principal que procesa requests de API Gateway
- **Property**: Modelo de datos para propiedades inmobiliarias
- **DatabaseUtil**: Utilidad para conexión a MySQL usando variables de entorno

### Flujo de Ejecución

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
4. Subir `target/lambda-property-get-all-1.0.0.jar`
5. Configurar handler: `com.softworld.handler.PropertyGetAllHandler`
6. Agregar variables de entorno
7. Configurar API Gateway trigger

### Opción 2: AWS CLI

```bash
# Crear función Lambda
aws lambda create-function \
  --function-name property-get-all \
  --runtime java17 \
  --handler com.softworld.handler.PropertyGetAllHandler \
  --zip-file fileb://target/lambda-property-get-all-1.0.0.jar \
  --role arn:aws:iam::YOUR_ACCOUNT:role/lambda-execution-role \
  --environment Variables="{DB_URL=your-db-url,DB_USER=your-user,DB_PASSWORD=your-password}"

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
    "id": 2,
    "ownerId": 102,
    "name": "Apartamento céntrico",
    "description": "Moderno apartamento en el centro",
    "propertyType": "Apartamento",
    "price": 150000.00,
    "available": true
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
