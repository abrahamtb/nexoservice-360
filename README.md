# NexoService 360

Backend académico y evolutivo para gestionar servicios técnicos, mantenimiento, reparación, instalación o calibración de equipos.

## Estado actual

Alcance minimo para presentacion academica:

- Frontend con Thymeleaf.
- 5 modulos web disponibles: Clientes, Equipos, Tecnicos, Servicios y Ordenes.
- 2 consultas web: equipos por cliente y ordenes por estado.
- 2 reportes PDF: clientes y ordenes de servicio.
- API REST documentada para pruebas en Postman.
- Inicio de sesion con Spring Security.
- Roles iniciales: ADMIN, RECEPCIONISTA y TECNICO.
- Dashboard inicial con resumen del sistema.
- Pantallas web amigables para errores y acceso denegado.

Documentacion de endpoints REST:

```text
docs/API_ENDPOINTS.md
```

Coleccion importable de Postman:

```text
docs/postman/NexoService_360.postman_collection.json
docs/postman/NexoService_360.postman_environment.json
```

Documentacion general del proyecto:

```text
docs/README_PROYECTO.md
docs/CODEX_CONTEXT.md
```

Carpetas de documentacion:

```text
docs/requerimientos
docs/diagramas
docs/base-datos
docs/manuales
```

Sprint 0 iniciado:

- Proyecto base Spring Boot creado.
- Configuración inicial para MySQL.
- Estructura preparada para crecer como monolito modular.
- Dependencias principales definidas en Maven.

Sprint 1 iniciado:

- Modulo Cliente implementado.
- CRUD basico de clientes.
- DTOs de entrada y salida.
- Validaciones con Bean Validation.
- Mapper manual.
- Service con reglas de negocio.
- Manejo global de errores.
- Busqueda y paginacion.

Sprint 2 iniciado:

- Modulo Equipo implementado.
- Relacion de equipo con cliente.
- Validacion de cliente existente y activo.
- Validacion de numero de serie no duplicado.
- Estados iniciales del equipo.

Sprint 3 iniciado:

- Modulo Tecnico implementado.
- Especialidad del tecnico.
- Disponibilidad del tecnico.
- Estado activo/inactivo.
- Validacion de correo no duplicado si se registra.

Sprint 4 iniciado:

- Modulo Servicio implementado.
- Nombre y descripcion del servicio.
- Precio base opcional.
- Duracion estimada opcional.
- Estado activo/inactivo.
- Validacion de nombre no duplicado.

Sprint 5 iniciado:

- Modulo OrdenServicio implementado.
- Registro de ordenes.
- Asignacion de tecnico.
- Estados de orden con reglas de transicion.
- Historial de cambios de estado.
- Validaciones entre cliente, equipo, tecnico y servicio.

Capa web academica iniciada:

- Vistas Thymeleaf para clientes, tecnicos y servicios.
- Vista Thymeleaf para equipos.
- Vista Thymeleaf para ordenes de servicio.
- Consultas Thymeleaf para equipos por cliente y ordenes por estado.
- Reporte PDF de clientes con JasperReports.
- Reporte PDF de ordenes con JasperReports.

## Tecnologías

- Java 17
- Spring Boot 3.x
- Maven
- Spring Web
- Thymeleaf
- Spring Data JPA
- Bean Validation
- MySQL
- Lombok
- JasperReports
- Spring Boot DevTools
- JUnit

## Base de datos

Base de datos inicial:

```sql
CREATE DATABASE nexoservice_db;
```

La conexión se configura mediante variables de entorno:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/nexoservice_db?useSSL=false&serverTimezone=America/Lima"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="tu_password"
```

Si no defines variables, la aplicación intentará conectarse a:

- URL: `jdbc:mysql://localhost:3306/nexoservice_db`
- Usuario: `root`
- Password: vacío

## Estructura de resources

Configuracion por perfiles:

```text
src/main/resources/application.properties
src/main/resources/application-dev.properties
src/main/resources/application-prod.properties
```

Estructura preparada:

```text
src/main/resources
├── db/migration
├── reports/jasper
├── static/css
├── static/js
├── static/img
├── static/icons
└── templates
    ├── auth
    ├── clientes
    ├── consultas
    ├── dashboard
    ├── equipos
    ├── error
    ├── fragments
    ├── ordenes
    ├── servicios
    └── tecnicos
```

La carpeta `db/migration` queda preparada para Flyway, pero Flyway aun no esta activo en esta version para no romper la base creada con `ddl-auto=update`.

## Estructura prevista

```text
com.nexoservice
├── auth
├── usuario
├── cliente
├── tecnico
├── equipo
├── servicio
├── orden
├── historial
├── config
├── security
├── exception
├── common
└── NexoServiceApplication.java
```

Cada módulo crecerá de forma incremental con capas como:

```text
controller
service
repository
entity
dto/request
dto/response
mapper
```

## Cómo ejecutar

Requisitos:

- Java 17
- Maven
- MySQL

Comandos:

```powershell
mvn spring-boot:run
```

Pruebas:

```powershell
mvn test
```

## Próximo paso

Completar y probar Sprint 5:

- Levantar MySQL.
- Ejecutar la aplicacion.
- Probar endpoints de clientes en Postman.
- Probar endpoints de equipos en Postman.
- Probar endpoints de tecnicos en Postman.
- Probar endpoints de servicios en Postman.
- Probar endpoints de ordenes en Postman.
- Probar pantallas Thymeleaf.
- Probar reportes PDF.
- Ajustar validaciones si aparece una regla nueva.
- Luego pasar al Sprint 6: seguridad con Spring Security, JWT, roles y permisos.

## Pantallas Thymeleaf para presentacion

Inicio:

```text
GET /
```

Login:

```text
GET /login
```

Dashboard:

```text
GET /dashboard
```

Acceso denegado:

```text
GET /access-denied
```

Usuarios demo:

```text
admin / admin123
recepcion / recep123
tecnico / tec123
```

CRUD Clientes:

```text
GET  /clientes
GET  /clientes/nuevo
POST /clientes
GET  /clientes/{id}/editar
POST /clientes/{id}
POST /clientes/{id}/desactivar
```

CRUD Equipos:

```text
GET  /equipos
GET  /equipos/nuevo
POST /equipos
GET  /equipos/{id}/editar
POST /equipos/{id}
POST /equipos/{id}/inactivar
```

CRUD Tecnicos:

```text
GET  /tecnicos
GET  /tecnicos/nuevo
POST /tecnicos
GET  /tecnicos/{id}/editar
POST /tecnicos/{id}
POST /tecnicos/{id}/desactivar
```

CRUD Servicios:

```text
GET  /servicios
GET  /servicios/nuevo
POST /servicios
GET  /servicios/{id}/editar
POST /servicios/{id}
POST /servicios/{id}/desactivar
```

Modulo web Ordenes:

```text
GET  /ordenes
GET  /ordenes/nueva
POST /ordenes
GET  /ordenes/{id}
POST /ordenes/{id}/cambiar-estado
```

Consultas:

```text
GET /consultas/equipos-por-cliente
GET /consultas/ordenes-por-estado
```

Reportes PDF:

```text
GET /reportes/clientes.pdf
GET /reportes/ordenes.pdf
```

## Modulo Cliente

Entidad principal:

```text
Cliente
```

Campos iniciales:

- id
- nombres
- apellidos
- tipoDocumento
- numeroDocumento
- correo
- telefono
- direccion
- activo
- fechaCreacion
- fechaActualizacion

Tipos de documento permitidos:

```text
DNI
RUC
CE
```

### Endpoints

Listar clientes:

```text
GET /api/clientes
```

Listar con busqueda y paginacion:

```text
GET /api/clientes?buscar=abraham&page=0&size=10
```

Buscar por id:

```text
GET /api/clientes/1
```

Crear cliente:

```text
POST /api/clientes
```

Actualizar cliente:

```text
PUT /api/clientes/1
```

Activar o desactivar cliente:

```text
PATCH /api/clientes/1/estado
```

### Ejemplos para Postman

Crear cliente:

```json
{
  "nombres": "Abraham",
  "apellidos": "Tintaya",
  "tipoDocumento": "DNI",
  "numeroDocumento": "12345678",
  "correo": "abraham@example.com",
  "telefono": "987654321",
  "direccion": "Lima, Peru"
}
```

Actualizar cliente:

```json
{
  "nombres": "Abraham",
  "apellidos": "Tintaya Ramos",
  "tipoDocumento": "DNI",
  "numeroDocumento": "12345678",
  "correo": "abraham.t@example.com",
  "telefono": "987654321",
  "direccion": "Lima, Peru"
}
```

Cambiar estado:

```json
{
  "activo": false
}
```

## Modulo OrdenServicio

Entidad principal:

```text
OrdenServicio
```

Entidad de historial:

```text
HistorialOrden
```

Campos iniciales de la orden:

- id
- cliente
- equipo
- servicio
- tecnico
- estado
- prioridad
- fechaIngreso
- fechaEstimada
- descripcionProblema
- observaciones
- fechaCreacion
- fechaActualizacion

Estados permitidos:

```text
RECIBIDA
ASIGNADA
EN_DIAGNOSTICO
DIAGNOSTICADA
EN_PROCESO
EN_ESPERA
FINALIZADA
ENTREGADA
CANCELADA
```

Prioridades permitidas:

```text
BAJA
MEDIA
ALTA
URGENTE
```

Reglas iniciales:

- La orden se crea en estado `RECIBIDA` si no tiene tecnico.
- Si se crea con tecnico, se crea en estado `ASIGNADA`.
- El cliente debe existir y estar activo.
- El equipo debe existir y pertenecer al cliente indicado.
- El equipo no debe estar `INACTIVO` ni `RETIRADO`.
- El servicio debe existir y estar activo.
- El tecnico debe existir, estar activo y estar `DISPONIBLE` para asignarse.
- Al asignar tecnico, el tecnico pasa a `OCUPADO`.
- Al finalizar, entregar o cancelar una orden, el tecnico vuelve a `DISPONIBLE`.
- Cada cambio de estado registra un item en `HistorialOrden`.
- Una orden `ENTREGADA` o `CANCELADA` no puede modificarse ni continuar su flujo normal.

Transiciones iniciales:

```text
RECIBIDA -> ASIGNADA, CANCELADA
ASIGNADA -> EN_DIAGNOSTICO, CANCELADA
EN_DIAGNOSTICO -> DIAGNOSTICADA, EN_ESPERA, CANCELADA
DIAGNOSTICADA -> EN_PROCESO, EN_ESPERA, CANCELADA
EN_PROCESO -> EN_ESPERA, FINALIZADA, CANCELADA
EN_ESPERA -> EN_DIAGNOSTICO, EN_PROCESO, CANCELADA
FINALIZADA -> ENTREGADA
ENTREGADA -> sin transiciones
CANCELADA -> sin transiciones
```

### Endpoints

Listar ordenes:

```text
GET /api/ordenes
```

Filtrar por estado:

```text
GET /api/ordenes?estado=RECIBIDA
```

Filtrar por cliente:

```text
GET /api/ordenes?clienteId=1
```

Filtrar por tecnico:

```text
GET /api/ordenes?tecnicoId=1
```

Buscar orden por id:

```text
GET /api/ordenes/1
```

Crear orden:

```text
POST /api/ordenes
```

Actualizar datos generales:

```text
PUT /api/ordenes/1
```

Asignar tecnico:

```text
PATCH /api/ordenes/1/asignar-tecnico
```

Cambiar estado:

```text
PATCH /api/ordenes/1/cambiar-estado
```

Consultar historial:

```text
GET /api/ordenes/1/historial
```

### Ejemplos para Postman

Crear orden sin tecnico:

```json
{
  "clienteId": 1,
  "equipoId": 1,
  "servicioId": 1,
  "prioridad": "MEDIA",
  "fechaEstimada": "2026-06-25",
  "descripcionProblema": "La laptop esta lenta y se apaga ocasionalmente",
  "observaciones": "Cliente entrega cargador original",
  "usuarioResponsable": "Recepcion"
}
```

Crear orden con tecnico:

```json
{
  "clienteId": 1,
  "equipoId": 1,
  "servicioId": 1,
  "tecnicoId": 1,
  "prioridad": "ALTA",
  "fechaEstimada": "2026-06-25",
  "descripcionProblema": "No enciende despues de una actualizacion",
  "observaciones": "Revisar fuente y placa",
  "usuarioResponsable": "Recepcion"
}
```

Asignar tecnico:

```json
{
  "tecnicoId": 1,
  "usuarioResponsable": "Recepcion",
  "observacion": "Tecnico asignado por disponibilidad"
}
```

Cambiar estado:

```json
{
  "estadoNuevo": "EN_DIAGNOSTICO",
  "usuarioResponsable": "Carlos Ramos",
  "observacion": "Se inicia revision del equipo"
}
```

Actualizar orden:

```json
{
  "prioridad": "ALTA",
  "fechaEstimada": "2026-06-26",
  "descripcionProblema": "La laptop esta lenta, se apaga y presenta sobrecalentamiento",
  "observaciones": "Cliente solicita respaldo antes de mantenimiento"
}
```

Respuesta de error por validacion:

```json
{
  "timestamp": "2026-06-18T18:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud contiene datos invalidos",
  "path": "/api/clientes",
  "validationErrors": {
    "nombres": "Los nombres son obligatorios"
  }
}
```

Respuesta de error por documento duplicado:

```json
{
  "timestamp": "2026-06-18T18:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un cliente con el documento DNI 12345678",
  "path": "/api/clientes",
  "validationErrors": null
}
```

## Modulo Equipo

Entidad principal:

```text
Equipo
```

Campos iniciales:

- id
- cliente
- tipo
- marca
- modelo
- numeroSerie
- descripcion
- estado
- fechaCreacion
- fechaActualizacion

Estados permitidos:

```text
ACTIVO
EN_SERVICIO
INACTIVO
RETIRADO
```

Reglas iniciales:

- Un equipo debe pertenecer a un cliente existente.
- No se puede registrar un equipo para un cliente inactivo.
- Si se registra numero de serie, no debe repetirse.
- Si el numero de serie se envia vacio, se guarda como `null`.

### Endpoints

Listar equipos:

```text
GET /api/equipos
```

Listar con busqueda y paginacion:

```text
GET /api/equipos?buscar=asus&page=0&size=10
```

Listar equipos de un cliente:

```text
GET /api/equipos/cliente/1
```

Buscar equipo por id:

```text
GET /api/equipos/1
```

Crear equipo:

```text
POST /api/equipos
```

Actualizar equipo:

```text
PUT /api/equipos/1
```

Cambiar estado del equipo:

```text
PATCH /api/equipos/1/estado
```

### Ejemplos para Postman

Crear equipo:

```json
{
  "clienteId": 1,
  "tipo": "Laptop",
  "marca": "ASUS",
  "modelo": "VivoBook 15",
  "numeroSerie": "ASUS-12345",
  "descripcion": "Laptop con cargador original"
}
```

Actualizar equipo:

```json
{
  "tipo": "Laptop",
  "marca": "ASUS",
  "modelo": "VivoBook 15 X515",
  "numeroSerie": "ASUS-12345",
  "descripcion": "Laptop con cargador original y funda"
}
```

Cambiar estado:

```json
{
  "estado": "EN_SERVICIO"
}
```

## Modulo Tecnico

Entidad principal:

```text
Tecnico
```

Campos iniciales:

- id
- nombres
- apellidos
- especialidad
- correo
- telefono
- disponibilidad
- activo
- fechaCreacion
- fechaActualizacion

Disponibilidades permitidas:

```text
DISPONIBLE
OCUPADO
EN_DESCANSO
NO_DISPONIBLE
```

Reglas iniciales:

- El tecnico se crea activo.
- El tecnico se crea con disponibilidad `DISPONIBLE`.
- Si se registra correo, no debe repetirse.
- Si el tecnico pasa a inactivo, su disponibilidad cambia a `NO_DISPONIBLE`.
- No se puede cambiar la disponibilidad de un tecnico inactivo.
- La relacion futura con `Usuario` se agregara cuando se implemente seguridad.

### Endpoints

Listar tecnicos:

```text
GET /api/tecnicos
```

Listar con busqueda y paginacion:

```text
GET /api/tecnicos?buscar=carlos&page=0&size=10
```

Listar tecnicos disponibles:

```text
GET /api/tecnicos/disponibles
```

Buscar tecnico por id:

```text
GET /api/tecnicos/1
```

Crear tecnico:

```text
POST /api/tecnicos
```

Actualizar tecnico:

```text
PUT /api/tecnicos/1
```

Activar o desactivar tecnico:

```text
PATCH /api/tecnicos/1/estado
```

Cambiar disponibilidad:

```text
PATCH /api/tecnicos/1/disponibilidad
```

### Ejemplos para Postman

Crear tecnico:

```json
{
  "nombres": "Carlos",
  "apellidos": "Ramos",
  "especialidad": "Mantenimiento de laptops",
  "correo": "carlos.ramos@example.com",
  "telefono": "987654322"
}
```

Actualizar tecnico:

```json
{
  "nombres": "Carlos",
  "apellidos": "Ramos Medina",
  "especialidad": "Diagnostico y mantenimiento de laptops",
  "correo": "carlos.ramos@example.com",
  "telefono": "987654322"
}
```

Cambiar estado:

```json
{
  "activo": false
}
```

Cambiar disponibilidad:

```json
{
  "disponibilidad": "OCUPADO"
}
```

## Modulo Servicio

Entidad principal:

```text
Servicio
```

Campos iniciales:

- id
- nombre
- descripcion
- precioBase
- duracionEstimadaMinutos
- activo
- fechaCreacion
- fechaActualizacion

Reglas iniciales:

- El servicio se crea activo.
- El nombre del servicio no debe repetirse.
- El precio base es opcional.
- El precio base no puede ser negativo.
- La duracion estimada es opcional.
- Si se registra duracion estimada, debe ser mayor a cero.
- Solo servicios activos deberian usarse al crear ordenes de servicio.

### Endpoints

Listar servicios:

```text
GET /api/servicios
```

Listar con busqueda y paginacion:

```text
GET /api/servicios?buscar=diagnostico&page=0&size=10
```

Listar servicios activos:

```text
GET /api/servicios/activos
```

Buscar servicio por id:

```text
GET /api/servicios/1
```

Crear servicio:

```text
POST /api/servicios
```

Actualizar servicio:

```text
PUT /api/servicios/1
```

Activar o desactivar servicio:

```text
PATCH /api/servicios/1/estado
```

### Ejemplos para Postman

Crear servicio:

```json
{
  "nombre": "Diagnostico y mantenimiento",
  "descripcion": "Revision general del equipo, limpieza interna y recomendaciones tecnicas",
  "precioBase": 80.00,
  "duracionEstimadaMinutos": 120
}
```

Actualizar servicio:

```json
{
  "nombre": "Diagnostico y mantenimiento preventivo",
  "descripcion": "Revision general, limpieza interna, cambio de pasta termica y recomendaciones",
  "precioBase": 100.00,
  "duracionEstimadaMinutos": 150
}
```

Cambiar estado:

```json
{
  "activo": false
}
```
