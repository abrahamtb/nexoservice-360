# NexoService 360 - Contexto maestro para Codex

## 1. Contexto del desarrollador

El desarrollador es estudiante de Computacion e Informatica en Cibertec y esta aprendiendo desarrollo backend con Java y Spring Boot.

Este proyecto sera:

- Proyecto academico principal.
- Parte del portafolio profesional.
- Base para seguir mejorando durante varios cursos.
- Base futura de una plataforma empresarial para servicios tecnicos y mantenimiento.
- Sistema adaptable a reparaciones, instalaciones, calibraciones, mantenimientos e inspecciones tecnicas.

Codex debe ayudar a desarrollar el proyecto y tambien explicar el codigo y las decisiones tomadas. No debe entregar codigo que el desarrollador no pueda comprender.

Las explicaciones deben ser claras, progresivas y apropiadas para alguien que conoce Java, SQL, MVC, JPA, APIs REST y bases de datos, pero todavia esta consolidando Spring Boot.

## 2. Descripcion del proyecto

Nombre provisional: **NexoService 360**

Nombre del backend: **NexoService API**

NexoService 360 es una plataforma web de gestion de servicios tecnicos y mantenimiento para empresas que reciben, instalan, inspeccionan, reparan o mantienen equipos y activos pertenecientes a sus clientes.

El sistema no debe limitarse a talleres de computadoras. Debe poder adaptarse a distintos rubros, por ejemplo:

- Talleres de computadoras.
- Empresas de aire acondicionado.
- Laboratorios de calibracion.
- Talleres automotrices.
- Empresas de mantenimiento industrial.
- Servicios tecnicos de electrodomesticos.
- Empresas que mantienen equipos medicos.

Los procesos principales que debe gestionar son:

- Reparaciones.
- Instalaciones.
- Calibraciones.
- Mantenimiento preventivo.
- Mantenimiento correctivo.
- Inspecciones tecnicas.

La vision a largo plazo es convertir NexoService 360 en un ERP vertical: un sistema empresarial especializado en companias que ofrecen servicios tecnicos y mantenimiento.

El sistema debe centralizar:

- Clientes.
- Activos o equipos del cliente.
- Tecnicos.
- Servicios.
- Ordenes de servicio.
- Diagnosticos.
- Historial de estados.
- Cotizaciones.
- Inventario y repuestos.
- Pagos.
- Reportes.
- Notificaciones.
- Auditoria.

A partir de ahora, cualquier modulo nuevo debe mantener nombres y reglas aplicables a diferentes rubros. Si se usa el termino `Equipo`, debe entenderse como sinonimo operativo de `Activo`, no como una computadora.

## 3. Flujo principal del negocio

Flujo general:

1. Se registra un cliente.
2. Se registra uno o mas activos o equipos pertenecientes al cliente.
3. Se crea una orden de servicio.
4. Se selecciona el tipo de servicio solicitado.
5. Se asigna un tecnico.
6. El tecnico inspecciona el activo.
7. El tecnico registra un diagnostico.
8. La orden cambia de estado.
9. Posteriormente se podra generar una cotizacion.
10. Se podran utilizar repuestos del inventario.
11. Se registra la finalizacion del trabajo.
12. Se genera un informe o comprobante.
13. Se registra la entrega del activo o el cierre del servicio.
14. Se conserva todo el historial de la atencion.

Ejemplo:

```text
Cliente: Abraham Tintaya
Activo: Aire acondicionado LG
Servicio: Mantenimiento preventivo
Tecnico: Carlos Ramos
Estado: EN_EJECUCION
Prioridad: MEDIA
Fecha estimada: 25/06/2026
```

## 4. Primera version o MVP

No se debe desarrollar todo al mismo tiempo.

La primera version tendra estos modulos:

- Roles.
- Usuarios.
- Clientes.
- Tecnicos.
- Activos o equipos del cliente.
- Tipos de activos.
- Servicios.
- Ordenes de servicio.
- Asignacion de tecnicos.
- Diagnosticos e inspecciones.
- Registro de trabajos realizados.
- Historial de estados de las ordenes.
- Dashboard basico.

Roles iniciales:

- ADMIN
- RECEPCIONISTA
- TECNICO

Equivalencias funcionales:

- `ADMIN` representa al ADMINISTRADOR.
- `RECEPCIONISTA` representa al RECEPCIONISTA o ASESOR.
- `TECNICO` representa al personal tecnico que atiende ordenes asignadas.
- `CLIENTE` queda previsto para una version futura.

Permisos generales:

ADMIN / ADMINISTRADOR:

- Gestionar usuarios.
- Gestionar tecnicos.
- Gestionar servicios.
- Consultar todos los modulos.
- Ver todas las ordenes.
- Consultar reportes.

RECEPCIONISTA / ASESOR:

- Registrar y actualizar clientes.
- Registrar activos o equipos.
- Crear ordenes.
- Asignar tecnicos.
- Consultar el estado de las ordenes.
- Registrar la entrega de activos o cierre de atenciones.

TECNICO:

- Consultar sus ordenes asignadas.
- Registrar diagnosticos.
- Registrar inspecciones y actividades realizadas.
- Agregar observaciones.
- Actualizar estados permitidos.
- Registrar la finalizacion de un trabajo.

Regla importante:

- Un tecnico solo puede modificar las ordenes que tenga asignadas. Esta validacion debe realizarse en la capa service, no solamente ocultando opciones en la interfaz.

## 5. Entidades iniciales

Entidades principales del MVP:

- Rol
- Usuario
- Cliente
- Tecnico
- Equipo o Activo
- TipoActivo
- Servicio
- OrdenServicio
- HistorialOrden

Relaciones iniciales:

- Un Rol puede pertenecer a muchos Usuarios.
- Un Usuario tiene un Rol.
- Un Usuario puede estar asociado a un Tecnico.
- Un Cliente puede tener muchos Activos o Equipos.
- Un Activo o Equipo pertenece a un Cliente.
- Una OrdenServicio pertenece a un Cliente.
- Una OrdenServicio pertenece a un Activo o Equipo.
- Una OrdenServicio puede tener un Tecnico asignado.
- Una OrdenServicio tiene un Servicio.
- Una OrdenServicio es registrada por un Usuario.
- Una OrdenServicio puede tener muchos registros en HistorialOrden.

La entidad central del sistema es `OrdenServicio`.

## 6. Estados iniciales de una orden

Estados oficiales sugeridos para el dominio:

- REGISTRADA
- RECIBIDA
- ASIGNADA
- EN_DIAGNOSTICO
- ESPERANDO_APROBACION
- EN_EJECUCION
- ESPERANDO_REPUESTO
- FINALIZADA
- LISTA_PARA_ENTREGA
- ENTREGADA
- CANCELADA

Usar `EN_EJECUCION` en lugar de nombres demasiado especificos como `EN_REPARACION`, porque una orden puede corresponder a instalacion, calibracion, mantenimiento o inspeccion.

Si el codigo existente usa estados anteriores como `DIAGNOSTICADA`, `EN_PROCESO` o `EN_ESPERA`, deben migrarse por etapas y sin romper funcionalidades que ya trabajan.

Reglas:

- No permitir cambios de estado sin reglas de negocio.
- Cada cambio de estado debe registrar un elemento en `HistorialOrden`.
- El historial debe incluir estado anterior, estado nuevo, fecha y hora, usuario responsable y observacion opcional.
- No se debe perder el historial anterior cuando cambia el estado de una orden.

## 7. Tecnologias iniciales

Tecnologias oficiales del proyecto:

- Java 17.
- Spring Boot 3.
- Maven.
- Spring Data JPA.
- Spring Security.
- MySQL.
- API REST.
- IntelliJ IDEA.
- Git y GitHub.

Tecnologias actuales de apoyo:

- Spring Web.
- Thymeleaf.
- Spring Data JPA.
- Bean Validation.
- Lombok.
- Spring Boot DevTools.
- Postman.
- JasperReports.

Tecnologias futuras:

- JWT.
- Swagger/OpenAPI.
- Spring Mail.
- MapStruct.
- JUnit.
- Mockito.
- Flyway.
- Docker.
- GitHub Actions.
- Spring Boot Actuator.

No agregar tecnologias futuras hasta que sean necesarias o se soliciten expresamente.

No cambiar la version de Java, Spring Boot o dependencias existentes sin explicar primero la razon.

## 8. Arquitectura

El sistema debe mantenerse como un monolito modular.

No implementar microservicios por ahora.

Paquete base:

```text
com.nexoservice
```

Estructura general:

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

Estructura sugerida por modulo:

```text
cliente
├── controller
├── service
├── repository
├── entity
├── dto
│   ├── request
│   └── response
└── mapper
```

Cuando sea util:

```text
service
├── ClienteService.java
└── impl
    └── ClienteServiceImpl.java
```

No crear capas innecesarias solo para aumentar cantidad de archivos.

Cada clase debe tener responsabilidad clara.

## 9. Flujo entre capas

Flujo HTTP:

```text
Cliente HTTP
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Base de datos
```

Flujo request:

```text
Request DTO
    ↓
Controller
    ↓
Service
    ↓
Entity
```

Flujo response:

```text
Entity
    ↓
Mapper
    ↓
Response DTO
    ↓
Controller
```

Reglas:

- Un controller no debe acceder directamente a un repository.
- Un controller no debe contener logica de negocio.
- Un repository no debe construir respuestas HTTP.
- Una entity no debe usarse directamente como respuesta publica de la API.
- Las validaciones de negocio van en la capa service.
- Las validaciones de formato van en los DTO mediante Bean Validation.
- Los mappers convierten entidades y DTO.
- Usar inyeccion por constructor.
- Evitar inyeccion por campos con `@Autowired`.

## 10. Convenciones de codigo

El dominio puede escribirse en espanol:

- Cliente
- Equipo
- Tecnico
- Servicio
- OrdenServicio
- HistorialOrden

Mantener nombres tecnicos estandar:

- controller
- service
- repository
- entity
- dto
- mapper
- exception
- config
- security

Convenciones Java:

- Clases en PascalCase.
- Metodos y atributos en camelCase.
- Constantes en MAYUSCULAS.
- Paquetes en minusculas.
- Enums en singular.

Evitar:

- Metodos excesivamente largos.
- Codigo duplicado.
- Variables como `x`, `dato`, `obj` o `temp`.
- Uso indiscriminado de `RuntimeException`.
- Respuestas genericas sin codigo HTTP correcto.
- Consultas SQL innecesarias si Spring Data JPA puede resolverlas.
- Relaciones bidireccionales que provoquen ciclos infinitos en JSON.

## 11. DTO y validaciones

No exponer entidades directamente.

Crear DTO separados cuando sea necesario:

- ClienteCreateRequest
- ClienteUpdateRequest
- ClienteResponse
- ClienteDetailResponse

Validaciones de ejemplo:

- Nombre obligatorio.
- DNI con formato valido.
- RUC con formato valido.
- Correo valido.
- Telefono valido.
- Numero de serie no duplicado cuando corresponda.
- Fecha estimada no anterior a la fecha de ingreso.
- Servicio activo.
- Tecnico activo.
- Cliente activo.
- Equipo perteneciente al cliente de la orden.

No confiar solo en validaciones del frontend.

## 12. Manejo de errores

Implementar manejo global con `@RestControllerAdvice`.

Excepciones sugeridas:

- ResourceNotFoundException
- BusinessException
- DuplicateResourceException
- InvalidStateTransitionException

Las respuestas de error deben ser claras y consistentes.

Ejemplo:

```json
{
  "timestamp": "2026-06-18T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontro el cliente con id 15",
  "path": "/api/clientes/15"
}
```

No devolver detalles internos innecesarios, contrasenas, consultas SQL ni trazas completas al cliente.

## 13. Base de datos

Base de datos inicial:

```text
nexoservice_db
```

Motor:

```text
MySQL
```

Tablas sugeridas:

- roles
- usuarios
- clientes
- tecnicos
- equipos
- servicios
- ordenes_servicio
- historial_orden

Incluir cuando corresponda:

- id
- estado o activo
- fechaCreacion
- fechaActualizacion

Para clientes, tecnicos, usuarios, servicios y equipos, considerar eliminacion logica cuando existan registros relacionados.

No eliminar informacion historica importante de ordenes.

## 14. Reglas iniciales del negocio

Reglas:

- No se puede registrar dos clientes con el mismo DNI o RUC.
- No se puede registrar dos usuarios con el mismo correo o username.
- Un equipo debe pertenecer a un cliente existente y activo.
- Una orden debe asociarse a un cliente y a uno de sus equipos.
- Un tecnico debe estar activo para ser asignado.
- Un servicio debe estar activo para utilizarse.
- Una orden entregada no debe volver a estados anteriores sin una operacion administrativa especial.
- Una orden cancelada no puede continuar su proceso normal.
- Cada cambio de estado debe generar historial.
- No debe eliminarse fisicamente una orden con informacion historica.
- Las contrasenas no deben almacenarse en texto plano cuando se implemente seguridad.
- El backend debe controlar todos los permisos importantes.

Si una regla no esta completamente definida, proponer una solucion razonable y explicarla antes de implementarla.

## 15. API REST

Disenar endpoints claros.

Ejemplos para clientes:

```text
GET    /api/clientes
GET    /api/clientes/{id}
POST   /api/clientes
PUT    /api/clientes/{id}
PATCH  /api/clientes/{id}/estado
```

Ejemplos para ordenes:

```text
GET    /api/ordenes
GET    /api/ordenes/{id}
POST   /api/ordenes
PUT    /api/ordenes/{id}
PATCH  /api/ordenes/{id}/asignar-tecnico
PATCH  /api/ordenes/{id}/cambiar-estado
GET    /api/ordenes/{id}/historial
```

Codigos HTTP:

- 200 para consultas y actualizaciones correctas.
- 201 para creacion.
- 204 cuando no se devuelve contenido.
- 400 para solicitudes invalidas.
- 401 para falta de autenticacion.
- 403 para falta de permisos.
- 404 cuando no existe un recurso.
- 409 para conflictos o duplicados.

No utilizar siempre `200 OK`.

## 16. Forma de trabajo con Codex

Antes de modificar codigo:

- Inspeccionar la estructura actual del repositorio.
- Revisar `pom.xml`.
- Revisar `application.properties` o `application.yml`.
- Identificar que modulos ya existen.
- No asumir que una clase existe sin comprobarla.
- No reemplazar archivos completos si solo se necesita un cambio pequeno.
- No eliminar codigo funcional sin explicar la razon.
- Respetar la arquitectura existente cuando sea compatible con estas reglas.

Para cada tarea:

- Explicar brevemente que se realizara.
- Enumerar los archivos que se crearan o modificaran.
- Implementar solamente el alcance solicitado.
- Explicar el flujo del codigo.
- Indicar como probarlo.
- Proporcionar ejemplos para Postman cuando corresponda.
- Senalar posibles errores o configuraciones necesarias.
- Sugerir un mensaje de commit.

Cuando aparezca un error:

- Leer el mensaje completo.
- Identificar la causa real.
- Explicar por que ocurre.
- Proponer el cambio minimo necesario.
- Evitar cambios aleatorios.
- Comprobar que el arreglo no rompa otras funcionalidades.

## 17. Nivel de explicacion esperado

No entregar solamente codigo.

Al crear una clase, explicar:

- Para que sirve.
- En que capa esta.
- Con que clases se comunica.
- Que anotaciones utiliza.
- Que datos recibe.
- Que datos devuelve.
- Como se prueba.

Explicar brevemente anotaciones nuevas como:

- `@Entity`
- `@Service`
- `@Repository`
- `@RestController`
- `@RequestMapping`
- `@Valid`
- `@Transactional`
- `@ManyToOne`
- `@OneToMany`

No agregar patrones avanzados si todavia no aportan valor real.

## 18. Desarrollo incremental

Trabajar por modulos y tareas pequenas.

### Alcance minimo de presentacion academica

El proyecto se esta escalando de forma progresiva porque el desarrollador esta terminando cuarto ciclo y desea reutilizarlo en ciclos posteriores.

Para una primera presentacion academica, el minimo requerido es:

- Frontend con Thymeleaf.
- Backend con al menos 3 CRUD.
- 2 consultas.
- 2 reportes PDF.

Implementacion elegida para ese minimo:

- 3 CRUD web: Clientes, Tecnicos y Servicios.
- 2 consultas web: equipos por cliente y ordenes por estado.
- 2 reportes PDF con JasperReports: clientes y ordenes de servicio.
- Inicio de sesion con Spring Security.
- Roles iniciales: ADMIN, RECEPCIONISTA y TECNICO.
- Dashboard inicial para presentar resumen del sistema.
- Manejo web amigable para errores de negocio, recursos no encontrados y acceso denegado.

Despues de cumplir ese minimo, el proyecto debe seguir escalando hacia seguridad, roles, ordenes mas completas, reportes avanzados, pruebas y despliegue.

### Estructura actual de resources

La carpeta `src/main/resources` ya esta organizada para crecer:

- `application.properties`, `application-dev.properties`, `application-prod.properties` para configuracion por perfil.
- `db/migration` reservado para futuras migraciones Flyway.
- `reports/jasper` para plantillas JasperReports.
- `static/css`, `static/js`, `static/img`, `static/icons` para assets web.
- `templates/fragments` reservado para reutilizar partes comunes de Thymeleaf.
- `templates/dashboard` para la pantalla principal.

Flyway todavia no esta activo. Se agregara cuando la version presentable este estable y se decida migrar desde `ddl-auto=update` a migraciones SQL versionadas.

Orden inicial recomendado:

### Sprint 0

- Crear proyecto.
- Configurar MySQL.
- Configurar Git.
- Preparar README.
- Verificar que la aplicacion inicia.

### Sprint 1

- Modulo Cliente.
- CRUD.
- Validaciones.
- DTO.
- Mapper.
- Manejo de errores.
- Paginacion y busqueda.

### Sprint 2

- Modulo Equipo.
- Relacion con Cliente.
- Validacion de propiedad.
- Estados del equipo.

### Sprint 3

- Modulo Tecnico.
- Especialidad.
- Disponibilidad.
- Estado activo.

### Sprint 4

- Modulo Servicio.
- Tipos de servicio.
- Precio base opcional.
- Duracion estimada.

### Sprint 5

- Modulo OrdenServicio.
- Registro.
- Asignacion.
- Estados.
- Historial.

### Sprint 6

- Spring Security.
- JWT.
- Roles y permisos.

### Sprint 7

- Swagger.
- Pruebas.
- JasperReports.
- Correos.
- Docker.

No construir todos los sprints en una sola respuesta o modificacion.

## 19. Funciones futuras

Funciones de etapas posteriores:

- Diagnosticos detallados.
- Cotizaciones.
- Inventario.
- Repuestos.
- Proveedores.
- Kardex.
- Pagos.
- Cierre de caja.
- Reportes Jasper.
- Notificaciones por correo.
- Recuperacion de contrasena.
- Verificacion de cuenta.
- Codigo QR para equipos.
- Aplicacion Android para tecnicos.
- Frontend web.
- Auditoria avanzada.
- Varias empresas y sucursales.
- Arquitectura multitenant.
- WebSockets.
- Despliegue en la nube.

No implementarlas antes de tiempo, pero evitar decisiones que hagan imposible agregarlas despues.

## 20. Seguridad

Cuando se implemente seguridad:

- Usar BCrypt para contrasenas.
- No devolver passwords en DTO.
- Usar JWT de manera segura.
- Aplicar autorizacion por roles.
- Proteger endpoints desde backend.
- No colocar secretos directamente en el repositorio.
- Usar variables de entorno.
- Configurar CORS de forma especifica.
- Validar archivos antes de almacenarlos.
- Evitar SQL Injection, XSS y exposicion de informacion sensible.

## 21. Git y documentacion

Sugerir commits claros:

```text
feat: implementar registro de clientes
feat: agregar historial de estados de orden
fix: validar cliente duplicado por documento
refactor: separar conversion de cliente en mapper
test: agregar pruebas del servicio de clientes
docs: documentar endpoints de clientes
```

Mantener actualizado el README con:

- Descripcion del proyecto.
- Tecnologias.
- Arquitectura.
- Instalacion.
- Configuracion.
- Base de datos.
- Endpoints.
- Estado actual.
- Proximas funcionalidades.

## 22. Primera instruccion al abrir el repositorio

Cuando Codex reciba acceso al proyecto:

- Analizar toda la estructura actual.
- Revisar dependencias.
- Identificar que partes ya estan implementadas.
- Indicar problemas o inconsistencias.
- Resumir el estado actual.
- Proponer el siguiente paso mas pequeno y logico.
- No generar todo el sistema automaticamente.
- No cambiar codigo hasta comprender como esta organizado.
- Si realiza cambios, hacerlos de manera incremental.
- Mantener siempre el objetivo de que NexoService 360 sea un proyecto academico profesional y escalable.

## 23. Nota operativa

Este archivo debe consultarse antes de tomar decisiones importantes del proyecto.

El objetivo no es avanzar rapido a costa de claridad, sino construir un backend profesional que el desarrollador pueda explicar, defender y mejorar.
