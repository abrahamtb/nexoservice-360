# NexoService API - Documentacion para Postman

Base URL local:

```text
http://localhost:8080
```

Header recomendado para requests con body:

```text
Content-Type: application/json
```

## Coleccion Postman importable

Archivos incluidos:

```text
docs/postman/NexoService_360.postman_collection.json
docs/postman/NexoService_360.postman_environment.json
```

Como importarlos:

1. Abre Postman.
2. Selecciona `Import`.
3. Importa la coleccion `NexoService_360.postman_collection.json`.
4. Importa el environment `NexoService_360.postman_environment.json`.
5. En la esquina superior derecha, selecciona el environment `NexoService 360 Local`.
6. Ejecuta la aplicacion en IntelliJ.
7. Prueba los requests desde la carpeta `01 Clientes`.

Variables del environment:

```text
baseUrl=http://localhost:8080
username=admin
password=admin123
clienteId=1
equipoId=1
tipoActivoId=1
tecnicoId=1
servicioId=1
ordenId=1
trabajoOrdenId=1
cotizacionId=1
productoInventarioId=1
pagoId=1
mantenimientoId=1
diagnosticoId=1
```

Despues de crear registros, actualiza los IDs con los valores reales que devuelva tu API.

La API esta protegida con Spring Security. La coleccion Postman usa Basic Auth con `username` y `password` del environment.

Usuarios demo:

```text
admin / admin123
recepcion / recep123
tecnico / tec123
```

## Codigos HTTP usados

- `200 OK`: consulta o actualizacion correcta.
- `201 Created`: recurso creado correctamente.
- `400 Bad Request`: datos invalidos o regla de negocio incumplida.
- `404 Not Found`: recurso no encontrado.
- `409 Conflict`: duplicado o conflicto con datos existentes.
- `500 Internal Server Error`: error inesperado.

## Formato de error

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

## Flujo recomendado de prueba

Prueba en este orden para tener datos relacionados:

1. Crear cliente.
2. Crear equipo para ese cliente.
3. Crear tecnico.
4. Crear servicio.
5. Crear orden de servicio.
6. Asignar tecnico o crear la orden con tecnico.
7. Cambiar estado de la orden.
8. Registrar diagnostico.
9. Consultar historial de la orden.
10. Probar reportes PDF desde navegador o Postman.

## Clientes

### Listar clientes

```http
GET /api/clientes
```

Query params opcionales:

```text
buscar=abraham
page=0
size=10
sort=id,asc
```

Ejemplo:

```http
GET /api/clientes?buscar=abraham&page=0&size=10
```

### Buscar cliente por ID

```http
GET /api/clientes/1
```

### Crear cliente

```http
POST /api/clientes
```

Body:

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

Respuesta esperada:

```text
201 Created
```

### Actualizar cliente

```http
PUT /api/clientes/1
```

Body:

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

### Activar o desactivar cliente

```http
PATCH /api/clientes/1/estado
```

Body:

```json
{
  "activo": false
}
```

## Activos o equipos

El modulo mantiene la ruta tecnica `/api/equipos`, pero debe entenderse como gestion de activos genericos del cliente.

## Tipos de activo

### Listar tipos de activo

```http
GET /api/tipos-activo
```

### Listar tipos de activo activos

```http
GET /api/tipos-activo/activos
```

### Buscar tipo de activo por ID

```http
GET /api/tipos-activo/1
```

### Crear tipo de activo

```http
POST /api/tipos-activo
```

Body:

```json
{
  "nombre": "Aire acondicionado",
  "descripcion": "Equipos de climatizacion residencial, comercial o industrial"
}
```

### Actualizar tipo de activo

```http
PUT /api/tipos-activo/1
```

Body:

```json
{
  "nombre": "Aire acondicionado",
  "descripcion": "Equipos split, ventana, cassette o sistemas de climatizacion"
}
```

### Activar o desactivar tipo de activo

```http
PATCH /api/tipos-activo/1/estado
```

Body:

```json
{
  "activo": false
}
```

Los tipos de activo se usan como catalogo para estandarizar el registro de activos/equipos.

### Listar activos/equipos

```http
GET /api/equipos
```

Ejemplo con busqueda:

```http
GET /api/equipos?buscar=asus&page=0&size=10
```

### Listar activos/equipos por cliente

```http
GET /api/equipos/cliente/1
```

### Buscar activo/equipo por ID

```http
GET /api/equipos/1
```

### Crear activo/equipo

```http
POST /api/equipos
```

Body:

```json
{
  "clienteId": 1,
  "tipoActivoId": 1,
  "tipo": "Aire acondicionado",
  "marca": "LG",
  "modelo": "Dual Inverter 12000 BTU",
  "numeroSerie": "LG-AC-12345",
  "codigoInterno": "ACT-001",
  "ubicacion": "Oficina principal",
  "caracteristicas": "Equipo split, 12000 BTU, 220V",
  "descripcion": "Activo entregado para mantenimiento preventivo",
  "observaciones": "Cliente reporta bajo rendimiento de enfriamiento"
}
```

Reglas:

- `clienteId` debe existir.
- El cliente debe estar activo.
- `tipoActivoId` es opcional, pero si se envia debe existir y estar activo.
- `numeroSerie` no debe repetirse si se envia.
- `codigoInterno`, `ubicacion`, `caracteristicas` y `observaciones` son opcionales para adaptar el activo a distintos rubros.

### Actualizar activo/equipo

```http
PUT /api/equipos/1
```

Body:

```json
{
  "tipoActivoId": 1,
  "tipo": "Aire acondicionado",
  "marca": "LG",
  "modelo": "Dual Inverter 12000 BTU",
  "numeroSerie": "LG-AC-12345",
  "codigoInterno": "ACT-001",
  "ubicacion": "Sala de reuniones",
  "caracteristicas": "Equipo split, 12000 BTU, 220V, control remoto incluido",
  "descripcion": "Activo actualizado despues de inspeccion inicial",
  "observaciones": "Requiere limpieza profunda de filtros"
}
```

### Cambiar estado de activo/equipo

```http
PATCH /api/equipos/1/estado
```

Body:

```json
{
  "estado": "EN_SERVICIO"
}
```

Estados permitidos:

```text
ACTIVO
EN_SERVICIO
INACTIVO
RETIRADO
```

## Tecnicos

### Listar tecnicos

```http
GET /api/tecnicos
```

Ejemplo con busqueda:

```http
GET /api/tecnicos?buscar=carlos&page=0&size=10
```

### Listar tecnicos disponibles

```http
GET /api/tecnicos/disponibles
```

### Buscar tecnico por ID

```http
GET /api/tecnicos/1
```

### Crear tecnico

```http
POST /api/tecnicos
```

Body:

```json
{
  "nombres": "Carlos",
  "apellidos": "Ramos",
  "especialidad": "Mantenimiento preventivo y correctivo",
  "correo": "carlos.ramos@example.com",
  "telefono": "987654322"
}
```

### Actualizar tecnico

```http
PUT /api/tecnicos/1
```

Body:

```json
{
  "nombres": "Carlos",
  "apellidos": "Ramos Medina",
  "especialidad": "Diagnostico, inspeccion y mantenimiento",
  "correo": "carlos.ramos@example.com",
  "telefono": "987654322"
}
```

### Activar o desactivar tecnico

```http
PATCH /api/tecnicos/1/estado
```

Body:

```json
{
  "activo": false
}
```

### Cambiar disponibilidad

```http
PATCH /api/tecnicos/1/disponibilidad
```

Body:

```json
{
  "disponibilidad": "OCUPADO"
}
```

Disponibilidades permitidas:

```text
DISPONIBLE
OCUPADO
EN_DESCANSO
NO_DISPONIBLE
```

## Servicios

### Listar servicios

```http
GET /api/servicios
```

Ejemplo con busqueda:

```http
GET /api/servicios?buscar=diagnostico&page=0&size=10
```

### Listar servicios activos

```http
GET /api/servicios/activos
```

### Buscar servicio por ID

```http
GET /api/servicios/1
```

### Crear servicio

```http
POST /api/servicios
```

Body:

```json
{
  "nombre": "Diagnostico y mantenimiento",
  "descripcion": "Revision general del equipo, limpieza interna y recomendaciones tecnicas",
  "precioBase": 80.00,
  "duracionEstimadaMinutos": 120
}
```

### Actualizar servicio

```http
PUT /api/servicios/1
```

Body:

```json
{
  "nombre": "Diagnostico y mantenimiento preventivo",
  "descripcion": "Revision general, limpieza interna, cambio de pasta termica y recomendaciones",
  "precioBase": 100.00,
  "duracionEstimadaMinutos": 150
}
```

### Activar o desactivar servicio

```http
PATCH /api/servicios/1/estado
```

Body:

```json
{
  "activo": false
}
```

## Ordenes de servicio

### Listar ordenes

```http
GET /api/ordenes
```

Filtros opcionales:

```text
estado=RECIBIDA
clienteId=1
tecnicoId=1
page=0
size=10
```

Ejemplos:

```http
GET /api/ordenes?estado=RECIBIDA
GET /api/ordenes?clienteId=1
GET /api/ordenes?tecnicoId=1
```

### Buscar orden por ID

```http
GET /api/ordenes/1
```

### Crear orden sin tecnico

```http
POST /api/ordenes
```

Body:

```json
{
  "clienteId": 1,
  "equipoId": 1,
  "servicioId": 1,
  "prioridad": "MEDIA",
  "fechaEstimada": "2026-06-25",
  "descripcionProblema": "El aire acondicionado presenta bajo rendimiento de enfriamiento",
  "observaciones": "Cliente solicita inspeccion general del activo",
  "usuarioResponsable": "Recepcion"
}
```

Estado inicial esperado:

```text
RECIBIDA
```

### Crear orden con tecnico

```http
POST /api/ordenes
```

Body:

```json
{
  "clienteId": 1,
  "equipoId": 1,
  "servicioId": 1,
  "tecnicoId": 1,
  "prioridad": "ALTA",
  "fechaEstimada": "2026-06-25",
  "descripcionProblema": "El activo no inicia correctamente despues de una intervencion previa",
  "observaciones": "Revisar alimentacion, componentes principales y configuracion",
  "usuarioResponsable": "Recepcion"
}
```

Estado inicial esperado:

```text
ASIGNADA
```

### Actualizar datos generales de orden

```http
PUT /api/ordenes/1
```

Body:

```json
{
  "prioridad": "ALTA",
  "fechaEstimada": "2026-06-26",
  "descripcionProblema": "El activo presenta bajo rendimiento y apagados durante la operacion",
  "observaciones": "Cliente solicita diagnostico antes de aprobar el servicio"
}
```

### Asignar tecnico

```http
PATCH /api/ordenes/1/asignar-tecnico
```

Body:

```json
{
  "tecnicoId": 1,
  "usuarioResponsable": "Recepcion",
  "observacion": "Tecnico asignado por disponibilidad"
}
```

### Cambiar estado

```http
PATCH /api/ordenes/1/cambiar-estado
```

Body:

```json
{
  "estadoNuevo": "EN_DIAGNOSTICO",
  "usuarioResponsable": "Carlos Ramos",
  "observacion": "Se inicia revision del equipo"
}
```

Transiciones principales:

```text
REGISTRADA -> RECIBIDA, CANCELADA
RECIBIDA -> ASIGNADA, CANCELADA
ASIGNADA -> EN_DIAGNOSTICO, CANCELADA
EN_DIAGNOSTICO -> ESPERANDO_APROBACION, ESPERANDO_REPUESTO, CANCELADA
ESPERANDO_APROBACION -> EN_EJECUCION, ESPERANDO_REPUESTO, CANCELADA
EN_EJECUCION -> ESPERANDO_REPUESTO, FINALIZADA, CANCELADA
ESPERANDO_REPUESTO -> EN_DIAGNOSTICO, EN_EJECUCION, CANCELADA
FINALIZADA -> LISTA_PARA_ENTREGA
LISTA_PARA_ENTREGA -> ENTREGADA
```

### Consultar historial de una orden

```http
GET /api/ordenes/1/historial
```

## Diagnosticos

### Buscar diagnostico por ID

```http
GET /api/diagnosticos/1
```

### Buscar diagnostico por orden

```http
GET /api/diagnosticos/orden/1
```

### Crear diagnostico

Antes de crear el diagnostico, la orden debe tener tecnico asignado y estar en estado `EN_DIAGNOSTICO`.

```http
POST /api/diagnosticos
```

Body:

```json
{
  "ordenServicioId": 1,
  "descripcionFalla": "El activo presenta bajo rendimiento durante la operacion",
  "causaProbable": "Filtros obstruidos y falta de mantenimiento preventivo",
  "solucionRecomendada": "Limpieza tecnica, inspeccion de componentes y prueba de funcionamiento",
  "observaciones": "Se recomienda mantenimiento preventivo cada 6 meses",
  "usuarioResponsable": "Carlos Ramos"
}
```

Respuesta esperada:

```text
201 Created
```

Regla importante:

- Al registrar el diagnostico, la orden pasa automaticamente de `EN_DIAGNOSTICO` a `ESPERANDO_APROBACION` y se agrega una entrada al historial.

### Actualizar diagnostico

```http
PUT /api/diagnosticos/1
```

Body:

```json
{
  "descripcionFalla": "El activo presenta bajo rendimiento durante la operacion",
  "causaProbable": "Filtros obstruidos y desgaste por uso continuo",
  "solucionRecomendada": "Limpieza tecnica, ajuste de componentes y prueba final",
  "observaciones": "Cliente autoriza continuar con el servicio",
  "usuarioResponsable": "Carlos Ramos"
}
```

## Trabajos realizados

### Listar trabajos por orden

```http
GET /api/trabajos-orden/orden/1
```

### Buscar trabajo por ID

```http
GET /api/trabajos-orden/1
```

### Crear trabajo realizado

La orden debe tener tecnico asignado y no debe estar `CANCELADA` ni `ENTREGADA`.

```http
POST /api/trabajos-orden
```

Body:

```json
{
  "ordenServicioId": 1,
  "tecnicoId": 1,
  "actividad": "Limpieza tecnica e inspeccion",
  "detalle": "Se realizo limpieza de filtros, revision de componentes y prueba de funcionamiento.",
  "tiempoInvertidoMinutos": 90,
  "observaciones": "El activo responde correctamente despues del mantenimiento.",
  "usuarioResponsable": "Carlos Ramos"
}
```

### Actualizar trabajo realizado

```http
PUT /api/trabajos-orden/1
```

Body:

```json
{
  "tecnicoId": 1,
  "actividad": "Limpieza tecnica, ajuste y prueba",
  "detalle": "Se completo limpieza, ajuste de componentes y prueba final de funcionamiento.",
  "tiempoInvertidoMinutos": 120,
  "observaciones": "Se recomienda seguimiento en el proximo mantenimiento.",
  "usuarioResponsable": "Carlos Ramos"
}
```

## Cotizaciones

### Listar cotizaciones

```http
GET /api/cotizaciones
```

Filtros opcionales:

```text
estado=PENDIENTE
ordenServicioId=1
```

### Buscar cotizacion por ID

```http
GET /api/cotizaciones/1
```

### Crear cotizacion

```http
POST /api/cotizaciones
```

Body:

```json
{
  "ordenServicioId": 1,
  "fechaVencimiento": "2026-06-30",
  "observaciones": "Cotizacion sujeta a aprobacion del cliente",
  "detalles": [
    {
      "descripcion": "Mantenimiento preventivo del activo",
      "cantidad": 1,
      "precioUnitario": 120.00
    },
    {
      "descripcion": "Limpieza tecnica y prueba de funcionamiento",
      "cantidad": 1,
      "precioUnitario": 80.00
    }
  ]
}
```

### Actualizar cotizacion

```http
PUT /api/cotizaciones/1
```

### Cambiar estado de cotizacion

```http
PATCH /api/cotizaciones/1/estado
```

Body:

```json
{
  "estado": "APROBADA"
}
```

Estados:

```text
PENDIENTE
APROBADA
RECHAZADA
VENCIDA
```

## Inventario y repuestos

### Listar productos de inventario

```http
GET /api/inventario/productos
```

Filtros opcionales:

```text
buscar=filtro
page=0
size=10
```

### Listar productos activos

```http
GET /api/inventario/productos/activos
```

### Buscar producto por ID

```http
GET /api/inventario/productos/1
```

### Crear producto

```http
POST /api/inventario/productos
```

Body:

```json
{
  "codigo": "REP-FILTRO-001",
  "nombre": "Filtro universal",
  "descripcion": "Repuesto para mantenimiento preventivo",
  "categoria": "Repuestos",
  "unidadMedida": "UND",
  "stockInicial": 10,
  "stockMinimo": 2,
  "costoUnitario": 15.00,
  "precioReferencia": 25.00
}
```

Reglas:

- `codigo` no debe repetirse.
- `stockInicial` y `stockMinimo` no pueden ser negativos.
- Si el stock inicial es mayor a cero, se registra un movimiento de entrada llamado `Stock inicial`.

### Actualizar producto

```http
PUT /api/inventario/productos/1
```

Body:

```json
{
  "codigo": "REP-FILTRO-001",
  "nombre": "Filtro universal actualizado",
  "descripcion": "Repuesto para mantenimiento preventivo y correctivo",
  "categoria": "Repuestos",
  "unidadMedida": "UND",
  "stockMinimo": 3,
  "costoUnitario": 16.00,
  "precioReferencia": 28.00
}
```

### Activar o desactivar producto

```http
PATCH /api/inventario/productos/1/estado
```

Body:

```json
{
  "activo": true
}
```

### Listar movimientos

```http
GET /api/inventario/movimientos
```

Filtros opcionales:

```text
productoId=1
ordenServicioId=1
```

### Registrar movimiento

```http
POST /api/inventario/movimientos
```

Body de entrada:

```json
{
  "productoId": 1,
  "tipoMovimiento": "ENTRADA",
  "cantidad": 5,
  "motivo": "Compra de repuestos",
  "usuarioResponsable": "Administrador"
}
```

Body de salida asociada a una orden:

```json
{
  "productoId": 1,
  "ordenServicioId": 1,
  "tipoMovimiento": "SALIDA",
  "cantidad": 1,
  "motivo": "Repuesto usado en orden de servicio",
  "usuarioResponsable": "Carlos Ramos"
}
```

Tipos de movimiento:

```text
ENTRADA
SALIDA
AJUSTE
```

## Pagos y estado de deuda

### Listar pagos

```http
GET /api/pagos
```

Filtros opcionales:

```text
ordenServicioId=1
estado=REGISTRADO
```

### Buscar pago por ID

```http
GET /api/pagos/1
```

### Consultar deuda de una orden

```http
GET /api/pagos/orden/1/deuda
```

Respuesta esperada:

```json
{
  "ordenServicioId": 1,
  "cotizacionAprobadaId": 1,
  "numeroCotizacion": "COT-000001",
  "total": 200.00,
  "pagado": 80.00,
  "saldo": 120.00,
  "estado": "PARCIAL"
}
```

Estados de deuda:

```text
SIN_COTIZACION_APROBADA
PENDIENTE
PARCIAL
PAGADO
```

### Registrar pago

La orden debe tener una cotizacion aprobada. Si no se envia `cotizacionId`, el sistema toma la ultima cotizacion aprobada de la orden.

```http
POST /api/pagos
```

Body:

```json
{
  "ordenServicioId": 1,
  "cotizacionId": 1,
  "monto": 80.00,
  "metodoPago": "YAPE",
  "numeroOperacion": "YP-123456",
  "observaciones": "Pago parcial del cliente",
  "usuarioResponsable": "Recepcion"
}
```

Metodos de pago:

```text
EFECTIVO
TARJETA
TRANSFERENCIA
YAPE
PLIN
OTRO
```

### Anular pago

```http
PATCH /api/pagos/1/anular
```

Body:

```json
{
  "motivo": "Registro duplicado",
  "usuarioResponsable": "Administrador"
}
```

Estados de pago:

```text
REGISTRADO
ANULADO
```

## Mantenimientos programados

### Listar planes de mantenimiento

```http
GET /api/mantenimientos-programados
```

Filtros opcionales:

```text
estado=ACTIVO
soloAlertas=true
```

### Crear plan preventivo

```http
POST /api/mantenimientos-programados
```

Body:

```json
{
  "equipoId": 1,
  "servicioId": 1,
  "frecuencia": "TRIMESTRAL",
  "fechaInicio": "2026-06-24",
  "proximaFecha": "2026-07-24",
  "observaciones": "Mantenimiento preventivo del activo"
}
```

Frecuencias:

```text
MENSUAL
TRIMESTRAL
SEMESTRAL
ANUAL
```

### Generar orden sugerida

```http
POST /api/mantenimientos-programados/1/generar-orden?usuarioResponsable=Recepcion
```

El sistema genera una orden de servicio con prioridad media y avanza la proxima fecha segun la frecuencia.

## Evidencias tecnicas

### Listar evidencias por orden

```http
GET /api/evidencias/orden/1
```

### Registrar evidencia

```http
POST /api/evidencias
```

Body:

```json
{
  "ordenServicioId": 1,
  "tecnicoId": 1,
  "tipo": "FOTO_ANTES",
  "nombreArchivo": "foto-antes.jpg",
  "urlArchivo": "/uploads/orden-1/foto-antes.jpg",
  "observaciones": "Estado del activo antes del mantenimiento",
  "usuarioResponsable": "Carlos Ramos"
}
```

Tipos:

```text
FOTO_ANTES
FOTO_DESPUES
ARCHIVO_ADJUNTO
OBSERVACION_TECNICA
FIRMA_CLIENTE
```

## Reportes PDF

Los reportes estan hechos con JasperReports.

### Reporte de clientes

```http
GET /reportes/clientes.pdf
```

### Reporte de ordenes

```http
GET /reportes/ordenes.pdf
```

### PDF de cotizacion

```http
GET /reportes/cotizaciones/1.pdf
```

Usa el `cotizacionId` real que devuelva la API.

### Recibo de pago PDF

```http
GET /reportes/pagos/1.pdf
```

Usa el `pagoId` real que devuelva la API.

## Pruebas de error recomendadas

### Cliente duplicado

Repite el mismo `POST /api/clientes` con el mismo documento.

Respuesta esperada:

```text
409 Conflict
```

### Validacion obligatoria

Envia un cliente sin `nombres`.

Respuesta esperada:

```text
400 Bad Request
```

### Equipo con cliente inexistente

Envia:

```json
{
  "clienteId": 999,
  "tipo": "Aire acondicionado",
  "marca": "LG"
}
```

Respuesta esperada:

```text
404 Not Found
```

### Orden con equipo que no pertenece al cliente

Usa un `clienteId` y un `equipoId` de otro cliente.

Respuesta esperada:

```text
400 Bad Request
```

### Transicion de estado no permitida

Intenta cambiar una orden de `RECIBIDA` directamente a `FINALIZADA`.

Respuesta esperada:

```text
400 Bad Request
```
