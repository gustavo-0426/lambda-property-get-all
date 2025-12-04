# 🏗️ Arquitectura Hexagonal - Guía Visual

## 📁 Estructura del Proyecto

```
src/main/java/com/softworld/hexagonal/
│
├── 🎯 domain/                          [NÚCLEO - Sin dependencias externas]
│   ├── model/
│   │   └── Property.java              ← ENTIDAD DEL DOMINIO
│   │
│   └── port/
│       ├── input/                     [PUERTOS DE ENTRADA]
│       │   └── GetAllPropertiesInputPort.java
│       │       ↑ Define QUÉ operaciones expone el sistema
│       │       ↑ Lo invoca: Adaptador de entrada
│       │       ↑ Lo implementa: Caso de uso (Service)
│       │
│       └── output/                    [PUERTOS DE SALIDA]
│           └── PropertyRepositoryOutputPort.java
│               ↑ Define CÓMO obtener datos externos
│               ↑ Lo usa: Caso de uso (Service)
│               ↑ Lo implementa: Adaptador de salida
│
├── 💼 application/                     [LÓGICA DE NEGOCIO]
│   └── service/
│       └── GetAllPropertiesService.java
│           ↑ CASO DE USO
│           ↑ Implementa: InputPort (puerto de entrada)
│           ↑ Usa: OutputPort (puerto de salida)
│
└── 🔧 infrastructure/                  [TECNOLOGÍAS CONCRETAS]
    ├── adapter/
    │   ├── input/                     [ADAPTADORES PRIMARIOS]
    │   │   └── lambda/
    │   │       └── PropertyLambdaInputAdapter.java
    │   │           ↑ ENTRADA a la aplicación (AWS Lambda)
    │   │           ↑ Invoca: InputPort (use case)
    │   │           ↑ Convierte: API Gateway Event → Dominio
    │   │
    │   └── output/                    [ADAPTADORES SECUNDARIOS]
    │       └── persistence/
    │           └── dynamodb/
    │               ├── DynamoDBPropertyRepositoryAdapter.java
    │               │   ↑ SALIDA a DynamoDB (NoSQL)
    │               │   ↑ Implementa: OutputPort (repository)
    │               │   ↑ Convierte: AttributeValue Map → Dominio
    │               │
    │               └── mapper/
    │                   └── PropertyDynamoDBMapper.java
    │                       ↑ Mapea DynamoDB AttributeValue → Property
    │                       ↑ Validaciones null-safe
    │
    └── configuration/
        └── DynamoDBConfiguration.java
            ↑ Configuración AWS SDK v2 DynamoDB Client
```

---

## 🔄 Flujo de Datos (Request → Response)

```
┌────────────────────────────────────────────────────────────────────┐
│  1. REQUEST EXTERNA                                                │
│     API Gateway Event (HTTP GET /properties)                       │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  2. INPUT ADAPTER (Adaptador Primario)                             │
│     PropertyLambdaInputAdapter                                     │
│     • Valida HTTP method y path                                    │
│     • Convierte APIGatewayProxyRequestEvent → llamada al dominio   │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           │ .getAllProperties()
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  3. INPUT PORT (Puerto de Entrada)                                 │
│     GetAllPropertiesInputPort                                      │
│     • Interface que define el caso de uso                          │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           │ implementa
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  4. USE CASE (Caso de Uso)                                         │
│     GetAllPropertiesService                                        │
│     • Ejecuta lógica de negocio                                    │
│     • No conoce tecnologías (ni Lambda, ni MySQL)                  │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           │ .findAll()
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  5. OUTPUT PORT (Puerto de Salida)                                 │
│     PropertyRepositoryOutputPort                                   │
│     • Interface que define acceso a datos                          │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           │ implementa
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  6. OUTPUT ADAPTER (Adaptador Secundario)                          │
│     DynamoDBPropertyRepositoryAdapter                              │
│     • Ejecuta Scan en DynamoDB usando AWS SDK v2                   │
│     • Convierte AttributeValue Map → Property (usando Mapper)      │
│     • Validaciones null-safe para campos opcionales                │
└──────────────────────────┬─────────────────────────────────────────┘
                           │
                           │ List<Property>
                           ▼
┌────────────────────────────────────────────────────────────────────┐
│  7. RESPUESTA                                                      │
│     • Viaja de vuelta por todos los niveles                        │
│     • Input Adapter convierte List<Property> → JSON (Gson)         │
│     • Retorna APIGatewayProxyResponseEvent con statusCode 200      │
└────────────────────────────────────────────────────────────────────┘
```

---

## 🎨 Identificación Visual Rápida

| Componente | Tipo | Nombre Archivo | Identificador Clave |
|------------|------|----------------|---------------------|
| **ENTIDAD** | Clase | `Property.java` | Sin lógica, solo getters/setters |
| **INPUT PORT** | Interface | `*InputPort.java` | Define operaciones del negocio |
| **OUTPUT PORT** | Interface | `*OutputPort.java` | Define operaciones de datos |
| **USE CASE** | Clase | `*Service.java` | Implementa InputPort, usa OutputPort |
| **INPUT ADAPTER** | Clase | `*InputAdapter.java` | Punto de entrada (Lambda, REST) |
| **OUTPUT ADAPTER** | Clase | `*OutputAdapter.java` | Implementa OutputPort (MySQL, etc) |
| **MAPPER** | Clase | `*Mapper.java` | Convierte datos externos ↔ dominio |
| **CONFIG** | Clase | `*Configuration.java` | Setup de tecnologías (AWS SDK v2) |

---

## 📋 Reglas de Dependencias

```
    ┌───────────────────────────────────────┐
    │      INPUT ADAPTER (Lambda)           │
    │   Conoce: AWS, JSON, HTTP             │
    └────────────────┬──────────────────────┘
                     │ depende de
                     ▼
    ┌───────────────────────────────────────┐
    │       INPUT PORT (Interface)          │
    │   Pertenece al DOMINIO                │
    └────────────────┬──────────────────────┘
                     │ implementado por
                     ▼
    ┌───────────────────────────────────────┐
    │      USE CASE (Service)               │
    │   Lógica de negocio pura              │
    └────────────────┬──────────────────────┘
                     │ usa
                     ▼
    ┌───────────────────────────────────────┐
    │      OUTPUT PORT (Interface)          │
    │   Pertenece al DOMINIO                │
    └────────────────┬──────────────────────┘
                     │ implementado por
                     ▼
    ┌───────────────────────────────────────┐
    │  OUTPUT ADAPTER (DynamoDBAdapter)     │
    │   Conoce: AWS SDK v2, DynamoDB        │
    └───────────────────────────────────────┘
```

**✅ Permitido:** Adaptadores → Puertos → Dominio  
**❌ Prohibido:** Dominio → Adaptadores

**Nota:** El dominio (`Property`) no tiene dependencias de DynamoDB, AWS, ni frameworks.

## 💡 Ventajas de esta Estructura

✅ **Identificación clara:** Los nombres de los archivos indican su rol  
✅ **Separación de concerns:** Cada capa tiene una responsabilidad única  
✅ **Testeable:** Puedes mockear OutputPort para tests unitarios  
✅ **Flexible:** Cambiar DynamoDB por otra BD solo afecta el OutputAdapter  
✅ **Independiente:** El dominio no conoce AWS, DynamoDB, ni frameworks  
✅ **Serverless-ready:** DynamoDB + Lambda = arquitectura 100% serverless  
✅ **Costo optimizado:** DynamoDB on-demand pricing, sin infraestructura fija
✅ **Flexible:** Cambiar MySQL por DynamoDB solo afecta el OutputAdapter  
✅ **Independiente:** El dominio no conoce AWS, MySQL, ni frameworks  

## 🚀 Para ejecutar

```bash
# Compilar
mvn clean compile

# Generar JAR para AWS Lambda
mvn clean package

# Handler para AWS Lambda
com.softworld.hexagonal.infrastructure.adapter.input.lambda.PropertyLambdaInputAdapter
```

---

## 🗄️ Configuración DynamoDB

### Variables de Entorno (Lambda)

```bash
AWS_REGION=us-east-1
DYNAMODB_TABLE_NAME=properties
```

### Estructura de la Tabla

- **Table Name:** `properties`
- **Partition Key:** `id` (Number)
- **Attributes:** ownerId, name, description, propertyType, price, available

### Permisos IAM Requeridos

El rol de ejecución de Lambda debe tener:
- `AmazonDynamoDBReadOnlyAccess` (para GET operations)

---

## 🔄 Ejemplo de Migración

Si necesitas cambiar de DynamoDB a otra base de datos:

**Solo modifica:**
1. `DynamoDBPropertyRepositoryAdapter.java` → Crear nuevo adaptador
2. `PropertyDynamoDBMapper.java` → Crear nuevo mapper
3. `DynamoDBConfiguration.java` → Nueva configuración

**NO modificas:**
- ✅ `Property.java` (dominio)
- ✅ `GetAllPropertiesInputPort.java` (puerto entrada)
- ✅ `PropertyRepositoryOutputPort.java` (puerto salida)
- ✅ `GetAllPropertiesService.java` (caso de uso)
- ✅ `PropertyLambdaInputAdapter.java` (handler Lambda)

**Esto es el poder de la Arquitectura Hexagonal** 🎯andler para AWS Lambda
com.softworld.hexagonal.infrastructure.adapter.input.lambda.PropertyLambdaInputAdapter
```
