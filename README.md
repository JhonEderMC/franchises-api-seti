# Franquicias API

API REST para la gestión de franquicias, sucursales y productos, desarrollada con Spring WebFlux y arquitectura limpia.

## Endpoints principales

### Franquicias
- **Crear franquicia**
  - `POST /api/v1/franchises`
- **Actualizar nombre de franquicia**
  - `PUT /api/v1/franchises/{franchiseId}/name`

### Sucursales
- **Agregar sucursal a franquicia**
  - `POST /api/v1/franchises/{franchiseId}/branches`
- **Actualizar nombre de sucursal**
  - `PUT /api/v1/branches/{branchId}/name`

### Productos
- **Agregar producto a sucursal**
  - `POST /api/v1/branches/{branchId}/products`
- **Eliminar producto de sucursal**
  - `DELETE /api/v1/branches/{branchId}/products/{productId}`
- **Actualizar stock de producto**
  - `PUT /api/v1/branches/{branchId}/products/{productId}/stock`
- **Actualizar nombre de producto**
  - `PUT /api/v1/branches/{branchId}/products/{productId}/name`
- **Obtener productos con mayor stock por franquicia**
  - `GET /api/v1/franchises/{franchiseId}/products/max-stock`

## Estructura del proyecto
- `RouterRest`: Define las rutas y asocia cada endpoint con su handler correspondiente.
- `FranchiseHandler`: Lógica para operaciones sobre franquicias.
- `BranchHandler`: Lógica para operaciones sobre sucursales.
- `ProductHandler`: Lógica para operaciones sobre productos.

## Tecnologías
- Java 21
- Spring WebFlux
- Gradle
- Docker

## Despliegue
El proyecto está listo para ser desplegado en Render o cualquier plataforma compatible con Docker. El puerto expuesto es el 8080.

## Ejemplo de uso
```bash
# Crear una franquicia
curl -X POST http://localhost:8080/api/v1/franchises -H "Content-Type: application/json" -d '{"name": "Mi Franquicia"}'

# Actualizar nombre de franquicia
curl -X PUT http://localhost:8080/api/v1/franchises/1/name -H "Content-Type: application/json" -d '{"name": "Nuevo Nombre"}'

# Agregar sucursal a una franquicia
curl -X POST http://localhost:8080/api/v1/franchises/1/branches -H "Content-Type: application/json" -d '{"name": "Sucursal Centro"}'

# Actualizar nombre de sucursal
curl -X PUT http://localhost:8080/api/v1/branches/1/name -H "Content-Type: application/json" -d '{"name": "Sucursal Norte"}'

# Agregar producto a sucursal
curl -X POST http://localhost:8080/api/v1/branches/1/products -H "Content-Type: application/json" -d '{"name": "Producto A", "stock": 100}'

# Eliminar producto de sucursal
curl -X DELETE http://localhost:8080/api/v1/branches/1/products/1

# Actualizar stock de producto
curl -X PUT http://localhost:8080/api/v1/branches/1/products/1/stock -H "Content-Type: application/json" -d '{"stock": 150}'

# Actualizar nombre de producto
curl -X PUT http://localhost:8080/api/v1/branches/1/products/1/name -H "Content-Type: application/json" -d '{"name": "Producto B"}'

# Obtener productos con mayor stock por franquicia
curl -X GET http://localhost:8080/api/v1/franchises/1/products/max-stock
```

---

Para más detalles, consulta la definición de rutas en `RouterRest` y la lógica en los handlers correspondientes.
