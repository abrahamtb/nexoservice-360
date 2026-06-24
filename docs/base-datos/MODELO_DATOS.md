# Modelo de datos - NexoService 360

## 1. Base de datos

Nombre sugerido:

```text
nexoservice_db
```

Motor:

```text
MySQL
```

## 2. Tablas principales

### roles

Guarda los roles del sistema.

Campos principales:

- id
- nombre

Valores iniciales:

- ADMIN
- RECEPCIONISTA
- TECNICO

### usuarios

Guarda los usuarios que pueden iniciar sesion.

Campos principales:

- id
- username
- correo
- password
- rol_id
- activo
- fecha_creacion
- fecha_actualizacion

Relacion:

- Muchos usuarios pertenecen a un rol.

### clientes

Guarda los datos de clientes.

Campos principales:

- id
- nombres
- apellidos
- tipo_documento
- numero_documento
- correo
- telefono
- direccion
- activo
- fecha_creacion
- fecha_actualizacion

Restriccion:

- No se debe repetir la combinacion tipo_documento + numero_documento.

### equipos

Guarda los equipos o activos de los clientes.

Campos principales:

- id
- cliente_id
- tipo
- marca
- modelo
- numero_serie
- descripcion
- estado
- fecha_creacion
- fecha_actualizacion

Relacion:

- Un cliente puede tener muchos equipos.
- Un equipo pertenece a un cliente.

Restriccion:

- El numero de serie no debe repetirse si se registra.

### tecnicos

Guarda los tecnicos del servicio.

Campos principales:

- id
- nombres
- apellidos
- especialidad
- correo
- telefono
- disponibilidad
- activo
- fecha_creacion
- fecha_actualizacion

Restriccion:

- El correo no debe repetirse si se registra.

### servicios

Guarda los tipos de servicios ofrecidos.

Campos principales:

- id
- nombre
- descripcion
- precio_base
- duracion_estimada_minutos
- activo
- fecha_creacion
- fecha_actualizacion

Restriccion:

- El nombre del servicio no debe repetirse.

### ordenes_servicio

Guarda las ordenes de servicio.

Campos principales:

- id
- cliente_id
- equipo_id
- servicio_id
- tecnico_id
- estado
- prioridad
- fecha_ingreso
- fecha_estimada
- descripcion_problema
- observaciones
- fecha_creacion
- fecha_actualizacion

Relaciones:

- Una orden pertenece a un cliente.
- Una orden pertenece a un equipo.
- Una orden usa un servicio.
- Una orden puede tener un tecnico asignado.

### historial_orden

Guarda los cambios de estado de una orden.

Campos principales:

- id
- orden_servicio_id
- estado_anterior
- estado_nuevo
- fecha_cambio
- usuario_responsable
- observacion

Relacion:

- Una orden puede tener muchos registros de historial.

## 3. Relaciones generales

```text
roles 1 --- N usuarios
clientes 1 --- N equipos
clientes 1 --- N ordenes_servicio
equipos 1 --- N ordenes_servicio
servicios 1 --- N ordenes_servicio
tecnicos 1 --- N ordenes_servicio
ordenes_servicio 1 --- N historial_orden
```

## 4. Enums principales

### TipoDocumento

```text
DNI
RUC
CE
```

### EstadoEquipo

```text
ACTIVO
EN_SERVICIO
INACTIVO
RETIRADO
```

### DisponibilidadTecnico

```text
DISPONIBLE
OCUPADO
EN_DESCANSO
NO_DISPONIBLE
```

### EstadoOrden

```text
RECIBIDA
ASIGNADA
EN_DIAGNOSTICO
ESPERANDO_APROBACION
EN_EJECUCION
ESPERANDO_REPUESTO
FINALIZADA
LISTA_PARA_ENTREGA
ENTREGADA
CANCELADA
```

### PrioridadOrden

```text
BAJA
MEDIA
ALTA
URGENTE
```

## 5. Modulos futuros

Tablas pendientes para etapas posteriores:

- diagnosticos
- inventario
- repuestos
- cotizaciones
- pagos
- notificaciones
- auditoria

Estas tablas se agregaran cuando los modulos correspondientes sean implementados.
