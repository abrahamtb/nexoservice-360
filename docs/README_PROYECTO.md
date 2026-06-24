# NexoService 360 - Documentacion del proyecto

## Descripcion general

NexoService 360 es una plataforma web para gestionar empresas que reciben, instalan, inspeccionan, reparan o mantienen activos y equipos de sus clientes. El sistema esta pensado para distintos rubros de servicios tecnicos y mantenimiento, como talleres de computadoras, aire acondicionado, calibracion, mantenimiento industrial, servicios automotrices, electrodomesticos o equipos medicos.

La vision del proyecto es crecer progresivamente hacia un ERP vertical especializado en companias de servicios tecnicos y mantenimiento.

## Objetivo academico

El proyecto se presenta como una aplicacion Spring Boot con:

- Frontend web con Thymeleaf.
- Backend con API REST.
- Conexion a MySQL.
- Seguridad con inicio de sesion, usuarios y roles.
- CRUD principales.
- Consultas.
- Reportes PDF con JasperReports.

## Alcance actual

Modulos implementados:

- Login y seguridad.
- Dashboard.
- Clientes.
- Equipos.
- Tecnicos.
- Servicios.
- Ordenes de servicio.
- Diagnostico detallado.
- Historial de estados.
- Reportes PDF.

Modulos pendientes para escalar:

- Tipos de activos.
- Registro de trabajos realizados.
- Inventario.
- Cotizaciones.
- Pagos.
- Notificaciones.
- Auditoria avanzada.
- Frontend moderno con React o Angular.

## Usuarios demo

```text
admin / admin123
recepcion / recep123
tecnico / tec123
```

## Rutas principales

```text
/login
/dashboard
/clientes
/equipos
/tecnicos
/servicios
/ordenes
/consultas/equipos-por-cliente
/consultas/ordenes-por-estado
/reportes/clientes.pdf
/reportes/ordenes.pdf
```

## Documentos relacionados

- `docs/CODEX_CONTEXT.md`: contexto maestro y reglas del proyecto.
- `docs/API_ENDPOINTS.md`: documentacion de endpoints para Postman.
- `docs/postman/`: coleccion y environment de Postman.
- `docs/requerimientos/REQUERIMIENTOS.md`: requisitos funcionales y no funcionales.
- `docs/diagramas/`: diagramas del sistema.
- `docs/base-datos/MODELO_DATOS.md`: entidades, tablas y relaciones principales.
- `docs/manuales/GUION_PRESENTACION.md`: orden recomendado para sustentar el proyecto.
- `docs/manuales/`: manuales de usuario o instalacion.
