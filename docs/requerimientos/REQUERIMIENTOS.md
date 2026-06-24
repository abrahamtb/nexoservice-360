# Requerimientos - NexoService 360

## 1. Objetivo del sistema

NexoService 360 tiene como objetivo gestionar el flujo principal de atencion de empresas de servicios tecnicos y mantenimiento: registro de clientes, activos o equipos, tecnicos, servicios, ordenes de servicio, diagnosticos, inspecciones, historial de estados y reportes.

El sistema debe poder adaptarse a distintos rubros, como talleres de computadoras, aire acondicionado, laboratorios de calibracion, talleres automotrices, mantenimiento industrial, electrodomesticos o equipos medicos. No debe disenarse exclusivamente para reparacion de computadoras.

## 2. Actores

### ADMIN

Usuario encargado de administrar la informacion principal del sistema.

Funciones principales:

- Gestionar tecnicos.
- Gestionar servicios.
- Consultar clientes, activos/equipos y ordenes.
- Ver reportes.
- Acceder al dashboard.

### RECEPCIONISTA

Usuario encargado de registrar la atencion inicial.

Funciones principales:

- Registrar clientes.
- Registrar activos o equipos.
- Crear ordenes de servicio.
- Consultar estados de ordenes.
- Revisar reportes basicos.

### TECNICO

Usuario encargado de atender las ordenes asignadas.

Funciones principales:

- Consultar ordenes.
- Cambiar estados permitidos.
- Registrar observaciones en el historial.

## 3. Requerimientos funcionales

### RF01 - Inicio de sesion

El sistema debe permitir que un usuario ingrese con usuario y contrasena.

Prioridad: Alta.

### RF02 - Gestion de roles

El sistema debe manejar roles iniciales:

- ADMIN
- RECEPCIONISTA
- TECNICO

Prioridad: Alta.

### RF03 - Gestion de clientes

El sistema debe permitir:

- Registrar clientes.
- Listar clientes.
- Buscar clientes.
- Actualizar clientes.
- Activar o desactivar clientes.

Prioridad: Alta.

### RF04 - Gestion de activos o equipos

El sistema debe permitir:

- Registrar activos o equipos asociados a clientes.
- Listar activos o equipos.
- Consultar activos o equipos por cliente.
- Actualizar activos o equipos.
- Cambiar estado del activo o equipo.

El termino `Equipo` puede usarse en la interfaz y en el codigo actual, pero debe entenderse como un activo generico del cliente.

Prioridad: Alta.

### RF05 - Gestion de tecnicos

El sistema debe permitir:

- Registrar tecnicos.
- Listar tecnicos.
- Consultar tecnicos disponibles.
- Actualizar tecnicos.
- Cambiar estado activo/inactivo.
- Cambiar disponibilidad.

Prioridad: Alta.

### RF06 - Gestion de servicios

El sistema debe permitir:

- Registrar servicios.
- Listar servicios.
- Consultar servicios activos.
- Actualizar servicios.
- Activar o desactivar servicios.

Prioridad: Alta.

### RF07 - Gestion de ordenes de servicio

El sistema debe permitir:

- Crear ordenes de servicio.
- Asociar cliente, activo/equipo y servicio.
- Asignar tecnico.
- Cambiar estado segun reglas de negocio.
- Consultar ordenes por estado.
- Consultar detalle de una orden.

Prioridad: Alta.

### RF08 - Historial de estados

El sistema debe registrar cada cambio de estado de una orden con:

- Estado anterior.
- Estado nuevo.
- Fecha y hora.
- Usuario responsable.
- Observacion.

Prioridad: Alta.

### RF09 - Dashboard

El sistema debe mostrar un dashboard con:

- Total de clientes.
- Total de activos o equipos.
- Total de tecnicos.
- Total de servicios.
- Total de ordenes.
- Ordenes por estado.

Prioridad: Media.

### RF10 - Reportes PDF

El sistema debe generar reportes PDF con JasperReports:

- Reporte de clientes.
- Reporte de ordenes de servicio.

Prioridad: Alta.

### RF11 - API REST para Postman

El sistema debe exponer endpoints REST para probar los modulos principales desde Postman.

Prioridad: Alta.

## 4. Requerimientos no funcionales

### RNF01 - Arquitectura

El sistema debe usar arquitectura por capas:

```text
Controller -> Service -> Repository -> Base de datos
```

### RNF02 - Base de datos

El sistema debe usar MySQL como motor de base de datos.

### RNF03 - Seguridad

Las contrasenas deben almacenarse cifradas con BCrypt.

### RNF04 - Validacion

El backend debe validar datos de entrada usando Bean Validation y reglas de negocio en la capa service.

### RNF05 - Mantenibilidad

El proyecto debe estar organizado por modulos funcionales.

### RNF06 - Escalabilidad academica

El proyecto debe permitir agregar nuevos modulos como inventario, cotizaciones, pagos, notificaciones y frontend moderno con React o Angular.

### RNF07 - Documentacion

El proyecto debe contar con documentacion para:

- Contexto del proyecto.
- Endpoints API.
- Requerimientos.
- Base de datos.
- Manuales.

## 5. Alcance actual

Incluido:

- Login.
- Dashboard.
- Clientes.
- Activos o equipos.
- Tecnicos.
- Servicios.
- Ordenes de servicio.
- Historial.
- Consultas.
- Reportes PDF.
- API REST documentada.

No incluido todavia:

- Diagnostico detallado.
- Inventario.
- Cotizaciones.
- Pagos.
- Notificaciones.
- Auditoria avanzada.
- JWT.
- Frontend React o Angular.

## 6. Reglas de negocio principales

- No se debe registrar dos clientes con el mismo documento.
- No se debe registrar dos activos/equipos con el mismo numero de serie o identificacion si se informa.
- No se debe registrar dos tecnicos con el mismo correo si se informa.
- No se debe registrar dos servicios con el mismo nombre.
- Una orden debe asociarse a cliente, activo/equipo y servicio.
- El activo/equipo debe pertenecer al cliente indicado.
- El servicio debe estar activo para usarse en una orden.
- El tecnico debe estar activo y disponible para asignarse.
- Cada cambio de estado debe registrar historial.
- Una orden entregada o cancelada no debe continuar el flujo normal.
