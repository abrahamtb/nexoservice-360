# Guion de presentacion - NexoService 360

## 1. Introduccion

Presentar el proyecto:

```text
NexoService 360 es un sistema web para gestionar servicios tecnicos de equipos.
Permite registrar clientes, equipos, tecnicos, servicios y ordenes de servicio,
manteniendo historial de estados y generando reportes PDF.
```

Mencionar que NexoService 360 no esta limitado a computadoras: es una plataforma para empresas que instalan, inspeccionan, reparan o mantienen activos de clientes. Puede adaptarse a rubros como aire acondicionado, calibracion, automotriz, mantenimiento industrial, electrodomesticos o equipos medicos.

## 2. Tecnologias usadas

- Java 17.
- Spring Boot 3.x.
- Spring Web.
- Spring Data JPA.
- Spring Security.
- Thymeleaf.
- MySQL.
- Bean Validation.
- Lombok.
- JasperReports.
- Postman.

## 3. Inicio de sesion

Ruta:

```text
http://localhost:8080/login
```

Credenciales:

```text
admin / admin123
```

Explicar:

- El sistema usa Spring Security.
- Las contrasenas se guardan con BCrypt.
- Existen roles: ADMIN, RECEPCIONISTA y TECNICO.

## 4. Dashboard

Ruta:

```text
http://localhost:8080/dashboard
```

Mostrar:

- Total de clientes.
- Total de activos o equipos.
- Total de tecnicos.
- Total de servicios.
- Total de ordenes.
- Ordenes agrupadas por estado.

Explicar que el dashboard permite ver un resumen rapido del sistema.

## 5. Flujo principal de demo

### Paso 1: Registrar cliente

Ruta:

```text
/clientes
```

Accion:

- Crear un cliente.
- Mostrar listado.
- Explicar validacion de documento unico.

### Paso 2: Registrar activo o equipo

Ruta:

```text
/equipos
```

Accion:

- Crear un activo/equipo asociado al cliente.
- Explicar relacion cliente-activo.
- Explicar validacion de numero de serie o identificacion.

### Paso 3: Registrar tecnico

Ruta:

```text
/tecnicos
```

Accion:

- Crear tecnico.
- Mostrar disponibilidad.
- Explicar que solo tecnicos activos y disponibles pueden asignarse a ordenes.

### Paso 4: Registrar servicio

Ruta:

```text
/servicios
```

Accion:

- Crear servicio.
- Mostrar precio base y duracion estimada.
- Explicar que solo servicios activos pueden usarse en ordenes.

### Paso 5: Crear orden de servicio

Ruta:

```text
/ordenes
```

Accion:

- Crear una orden seleccionando cliente, activo/equipo y servicio.
- Opcionalmente asignar tecnico.
- Mostrar estado inicial.

Explicar:

- Si no tiene tecnico inicia en `RECIBIDA`.
- Si tiene tecnico inicia en `ASIGNADA`.

### Paso 6: Cambiar estado y ver historial

Ruta:

```text
/ordenes/{id}
```

Accion:

- Cambiar estado de la orden.
- Mostrar historial.

Explicar:

- Cada cambio registra estado anterior, estado nuevo, fecha, usuario y observacion.
- El sistema no permite transiciones invalidas.

## 6. Consultas requeridas

### Consulta 1: Equipos por cliente

Ruta:

```text
/consultas/equipos-por-cliente
```

Mostrar:

- Seleccionar cliente.
- Ver equipos asociados.

### Consulta 2: Ordenes por estado

Ruta:

```text
/consultas/ordenes-por-estado
```

Mostrar:

- Seleccionar estado.
- Ver ordenes con ese estado.

## 7. Reportes PDF

### Reporte de clientes

Ruta:

```text
/reportes/clientes.pdf
```

### Reporte de ordenes

Ruta:

```text
/reportes/ordenes.pdf
```

Explicar:

- Los reportes estan generados con JasperReports.
- Las plantillas se encuentran en `src/main/resources/reports/jasper`.

## 8. API REST y Postman

Mostrar que tambien existe API REST documentada:

```text
docs/API_ENDPOINTS.md
```

Coleccion Postman:

```text
docs/postman/NexoService_360.postman_collection.json
```

Explicar:

- La API permite probar el backend sin depender de Thymeleaf.
- Postman usa Basic Auth con usuario demo.

## 9. Puntos tecnicos para defender

- Arquitectura por capas: Controller, Service, Repository.
- DTOs para no exponer entidades directamente.
- Validaciones con Bean Validation.
- Reglas de negocio en la capa service.
- Manejo global de errores REST.
- Manejo amigable de errores web.
- Seguridad con roles.
- Reportes PDF con JasperReports.
- Base de datos MySQL.

## 10. Futuras mejoras

- Diagnostico detallado.
- Inventario.
- Cotizaciones.
- Pagos.
- Notificaciones.
- Auditoria.
- JWT para API.
- Frontend con React o Angular.
- Migraciones con Flyway.
