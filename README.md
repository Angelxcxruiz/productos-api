# 🛒 Productos API — Spring WebFlux + R2DBC + PostgreSQL

API REST **100% reactiva** construida con Spring Boot WebFlux y R2DBC. Lista para desplegar en **Render**.

---

## 🚀 Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje |
| Spring Boot | 3.2.3 | Framework |
| Spring WebFlux | 6.x | HTTP Reactivo (Netty) |
| R2DBC | — | Driver BD reactivo |
| PostgreSQL | 15+ | Base de datos |
| Lombok | — | Reducir boilerplate |

---

## 📁 Estructura del Proyecto

```
src/main/java/com/api/productos/
├── ProductosApiApplication.java   # Punto de entrada
├── controller/
│   └── ProductoController.java    # Endpoints REST
├── service/
│   └── ProductoService.java       # Lógica de negocio
├── repository/
│   └── ProductoRepository.java    # Acceso a datos (R2DBC)
├── model/
│   └── Producto.java              # Entidad
├── dto/
│   └── ProductoDTO.java           # Request / Response
└── exception/
    ├── ProductoNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 📡 Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/productos` | Lista todos los productos activos |
| `GET` | `/api/v1/productos/{id}` | Obtiene un producto por ID |
| `GET` | `/api/v1/productos/categoria/{cat}` | Filtra por categoría |
| `GET` | `/api/v1/productos/buscar?nombre=x` | Búsqueda parcial por nombre |
| `GET` | `/api/v1/productos/precio?min=x&max=y` | Filtra por rango de precio |
| `POST` | `/api/v1/productos` | Crea un nuevo producto |
| `PUT` | `/api/v1/productos/{id}` | Actualiza un producto |
| `DELETE` | `/api/v1/productos/{id}` | Soft delete (activo = false) |

> El endpoint `GET /api/v1/productos` también soporta **Server-Sent Events (SSE)**  
> enviando el header `Accept: text/event-stream`.

### Ejemplo POST `/api/v1/productos`

```json
{
  "nombre": "Laptop Gamer Pro",
  "descripcion": "Laptop con RTX 4060 y 32GB RAM",
  "precio": 1299.99,
  "stock": 10,
  "categoria": "Electrónica",
  "activo": true
}
```

---

## ⚙️ Variables de Entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DATABASE_URL` | URL R2DBC de PostgreSQL | `r2dbc:postgresql://host:5432/db` |
| `DATABASE_USERNAME` | Usuario de la base de datos | `postgres` |
| `DATABASE_PASSWORD` | Contraseña de la base de datos | `secret` |
| `PORT` | Puerto del servidor | `8080` |

---

## 🏃 Correr Localmente

### Requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL corriendo en `localhost:5432`

### Pasos

```bash
# 1. Crear la base de datos
psql -U postgres -c "CREATE DATABASE productosdb;"

# 2. Clonar y compilar
git clone <tu-repo>
cd reactive-api
mvn clean package -DskipTests

# 3. Correr
java -jar target/productos-0.0.1-SNAPSHOT.jar
```

O con variables de entorno personalizadas:

```bash
DATABASE_URL=r2dbc:postgresql://localhost:5432/productosdb \
DATABASE_USERNAME=postgres \
DATABASE_PASSWORD=mipassword \
java -jar target/productos-0.0.1-SNAPSHOT.jar
```

### Con Docker

```bash
docker build -t productos-api .
docker run -p 8080:8080 \
  -e DATABASE_URL=r2dbc:postgresql://host.docker.internal:5432/productosdb \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=postgres \
  productos-api
```

---

## ☁️ Desplegar en Render

### Opción A — Automático con `render.yaml` (Blueprint)

1. Sube el proyecto a GitHub
2. En Render: **New → Blueprint**
3. Conecta tu repositorio
4. Render detectará el `render.yaml` y creará el servicio web + PostgreSQL automáticamente

### Opción B — Manual

1. **Crear Base de Datos PostgreSQL** en Render:
   - New → PostgreSQL → Free plan
   - Anota: `Host`, `Port`, `Database`, `Username`, `Password`

2. **Crear Web Service** en Render:
   - New → Web Service → Docker
   - Conecta tu repositorio de GitHub
   - Configura las variables de entorno:

```
DATABASE_URL     = r2dbc:postgresql://<host>:<port>/<database>
DATABASE_USERNAME = <usuario>
DATABASE_PASSWORD = <contraseña>
PORT             = 8080
```

> ⚠️ **Importante:** Render te da la URL en formato `postgresql://...`  
> Debes cambiarla a `r2dbc:postgresql://...` (solo cambia el prefijo).

3. **Deploy** → Render construirá la imagen Docker y levantará el servicio.

---

## 🗄️ Esquema de Base de Datos

```sql
CREATE TABLE productos (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(150)   NOT NULL,
    descripcion TEXT,
    precio      NUMERIC(10, 2) NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0,
    categoria   VARCHAR(100),
    activo      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);
```

> El esquema se aplica automáticamente al iniciar la app gracias a `schema.sql`.
