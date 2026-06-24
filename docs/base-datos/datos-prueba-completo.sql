USE nexoservice_db;

-- Datos de prueba completos para NexoService 360.
-- Ejecuta primero la aplicacion una vez para que Hibernate cree/actualice las tablas.
-- Los usuarios demo principales se crean desde DataInitializer:
-- admin/admin123, recepcion/recep123, tecnico/tec123.
-- Este script es mayormente idempotente: evita duplicar catalogos y datos principales.

-- =========================================================
-- 1. Roles base
-- =========================================================

INSERT IGNORE INTO roles (nombre)
VALUES
('ADMINISTRADOR'),
('ADMIN'),
('RECEPCIONISTA'),
('TECNICO'),
('CLIENTE');

-- =========================================================
-- 2. Catalogos administrativos
-- =========================================================

INSERT IGNORE INTO tipos_activo
(nombre, descripcion, activo, fecha_creacion)
VALUES
('Laptop', 'Equipos portatiles de uso personal o corporativo', true, NOW()),
('PC Escritorio', 'Computadoras de escritorio y estaciones de trabajo', true, NOW()),
('Impresora', 'Impresoras multifuncionales, tinta o laser', true, NOW()),
('Router', 'Equipos de red, routers y access points', true, NOW()),
('Aire acondicionado', 'Equipos de climatizacion residencial o empresarial', true, NOW()),
('Camara de seguridad', 'Camaras IP, DVR, NVR y accesorios CCTV', true, NOW());

INSERT IGNORE INTO servicios
(nombre, descripcion, precio_base, duracion_estimada_minutos, activo, fecha_creacion)
VALUES
('Diagnostico tecnico', 'Revision inicial del activo y deteccion de falla.', 35.00, 45, true, NOW()),
('Mantenimiento preventivo', 'Limpieza, revision general y pruebas de funcionamiento.', 80.00, 120, true, NOW()),
('Mantenimiento correctivo', 'Correccion de fallas detectadas durante el diagnostico.', 140.00, 180, true, NOW()),
('Instalacion y configuracion', 'Instalacion, configuracion y puesta en marcha del activo.', 120.00, 150, true, NOW()),
('Cambio de repuesto', 'Instalacion de repuesto y prueba posterior.', 95.00, 90, true, NOW()),
('Configuracion de red', 'Configuracion de router, impresora compartida o red local.', 90.00, 90, true, NOW());

-- =========================================================
-- 3. Clientes
-- =========================================================

INSERT IGNORE INTO clientes
(nombres, apellidos, tipo_documento, numero_documento, correo, telefono, direccion, activo, fecha_creacion)
VALUES
('Abraham', 'Tintaya', 'DNI', '76543210', 'abraham.tintaya@gmail.com', '987654321', 'Av. Los Proceres 123, Lima', true, NOW()),
('Maria', 'Quispe Ramos', 'DNI', '70123456', 'maria.quispe@gmail.com', '956123789', 'Jr. Las Flores 450, San Juan de Lurigancho', true, NOW()),
('Carlos', 'Mamani Torres', 'DNI', '73456789', 'carlos.mamani@gmail.com', '945678123', 'Av. Universitaria 220, Los Olivos', true, NOW()),
('Lucia', 'Fernandez Rios', 'DNI', '70889911', 'lucia.fernandez@gmail.com', '933444555', 'Calle Central 321, Surco', true, NOW()),
('TecnoMarket Peru', NULL, 'RUC', '20604567891', 'contacto@tecnomarket.pe', '014567890', 'Av. Arequipa 1500, Lince', true, NOW()),
('Clinica San Miguel', NULL, 'RUC', '20599887766', 'sistemas@clinicasanmiguel.pe', '016667788', 'Av. La Marina 900, San Miguel', true, NOW());

-- =========================================================
-- 4. Tecnicos
-- =========================================================

INSERT IGNORE INTO tecnicos
(nombres, apellidos, especialidad, correo, telefono, disponibilidad, activo, fecha_creacion)
VALUES
('Luis', 'Salazar Vega', 'Soporte de laptops y PCs', 'luis.salazar@nexoservice.com', '955111222', 'DISPONIBLE', true, NOW()),
('Karla', 'Paredes Soto', 'Redes y mantenimiento preventivo', 'karla.paredes@nexoservice.com', '955333444', 'DISPONIBLE', true, NOW()),
('Miguel', 'Rojas Flores', 'Impresoras y perifericos', 'miguel.rojas@nexoservice.com', '955555666', 'OCUPADO', true, NOW()),
('Andrea', 'Lopez Medina', 'Climatizacion y equipos electricos', 'andrea.lopez@nexoservice.com', '944222111', 'DISPONIBLE', true, NOW()),
('Tecnico', 'Demo', 'Diagnostico y reparacion', 'tecnico@nexoservice.com', '955777888', 'DISPONIBLE', true, NOW());

-- =========================================================
-- 5. Activos / Equipos
-- =========================================================

INSERT IGNORE INTO equipos
(cliente_id, tipo_activo_id, tipo, marca, modelo, numero_serie, codigo_interno, ubicacion, caracteristicas, descripcion, observaciones, estado, fecha_creacion)
VALUES
((SELECT id FROM clientes WHERE numero_documento = '76543210'), (SELECT id FROM tipos_activo WHERE nombre = 'Laptop'), 'Laptop', 'ASUS', 'VivoBook 15', 'ASUS-VB15-001', 'ACT-LAP-001', 'Domicilio', 'Core i5, 8GB RAM, 512GB SSD', 'Laptop personal color gris, cargador incluido.', 'Cliente reporta apagados inesperados.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '70123456'), (SELECT id FROM tipos_activo WHERE nombre = 'Laptop'), 'Laptop', 'Lenovo', 'IdeaPad 3', 'LEN-IP3-002', 'ACT-LAP-002', 'Domicilio', 'Ryzen 5, 8GB RAM, 1TB HDD', 'Laptop con lentitud al iniciar Windows.', 'Se recomienda revisar disco.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '73456789'), (SELECT id FROM tipos_activo WHERE nombre = 'PC Escritorio'), 'PC Escritorio', 'HP', 'ProDesk 400', 'HP-PD400-003', 'ACT-PC-003', 'Oficina', 'Core i3, 4GB RAM, HDD 500GB', 'CPU de oficina, no enciende correctamente.', 'Equipo recibido sin monitor.', 'EN_SERVICIO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '20604567891'), (SELECT id FROM tipos_activo WHERE nombre = 'Impresora'), 'Impresora', 'Epson', 'L3250', 'EPS-L3250-004', 'ACT-IMP-004', 'Administracion', 'Multifuncional tinta continua', 'Impresora multifuncional con error de impresion.', 'Cliente requiere prueba de color.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '20604567891'), (SELECT id FROM tipos_activo WHERE nombre = 'Router'), 'Router', 'TP-Link', 'Archer C6', 'TPL-C6-005', 'ACT-RED-005', 'Rack principal', 'Router doble banda AC1200', 'Router principal de oficina.', 'Requiere configuracion de red.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '20599887766'), (SELECT id FROM tipos_activo WHERE nombre = 'Aire acondicionado'), 'Aire acondicionado', 'LG', 'Dual Inverter 12000 BTU', 'LG-AC-006', 'ACT-AC-006', 'Sala de espera', 'Equipo split 220V', 'Aire acondicionado con bajo rendimiento.', 'Filtro saturado.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE numero_documento = '20599887766'), (SELECT id FROM tipos_activo WHERE nombre = 'Camara de seguridad'), 'Camara de seguridad', 'Hikvision', 'DS-2CD1023G0', 'HK-CAM-007', 'ACT-CCTV-007', 'Entrada principal', 'Camara IP 2MP exterior', 'Camara sin visualizacion remota.', 'Revisar red y fuente.', 'ACTIVO', NOW());

-- =========================================================
-- 6. Ordenes de servicio en distintos estados
-- =========================================================

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'ASIGNADA', 'ALTA', NOW(), DATE_ADD(CURDATE(), INTERVAL 2 DAY),
       'La laptop se apaga despues de unos minutos de uso.',
       'Cliente indica que ocurre al abrir varias aplicaciones.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'ASUS-VB15-001'
JOIN servicios s ON s.nombre = 'Diagnostico tecnico'
JOIN tecnicos t ON t.correo = 'luis.salazar@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'La laptop se apaga despues de unos minutos de uso.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'EN_DIAGNOSTICO', 'MEDIA', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY),
       'El equipo esta muy lento y demora en iniciar.',
       'Revisar disco, memoria y programas de inicio.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'LEN-IP3-002'
JOIN servicios s ON s.nombre = 'Mantenimiento preventivo'
JOIN tecnicos t ON t.correo = 'tecnico@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'El equipo esta muy lento y demora en iniciar.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'ESPERANDO_APROBACION', 'URGENTE', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY),
       'La PC no enciende y emite pitidos al presionar el boton.',
       'Pendiente de aprobacion de cotizacion.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'HP-PD400-003'
JOIN servicios s ON s.nombre = 'Diagnostico tecnico'
JOIN tecnicos t ON t.correo = 'luis.salazar@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'La PC no enciende y emite pitidos al presionar el boton.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'EN_EJECUCION', 'MEDIA', DATE_SUB(NOW(), INTERVAL 3 DAY), CURDATE(),
       'La impresora no imprime en color y muestra alerta de mantenimiento.',
       'Cliente aprobo limpieza de cabezales.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'EPS-L3250-004'
JOIN servicios s ON s.nombre = 'Mantenimiento correctivo'
JOIN tecnicos t ON t.correo = 'miguel.rojas@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'La impresora no imprime en color y muestra alerta de mantenimiento.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'ESPERANDO_REPUESTO', 'ALTA', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY),
       'El router pierde conexion durante la jornada laboral.',
       'Pendiente de reemplazo de fuente de poder.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'TPL-C6-005'
JOIN servicios s ON s.nombre = 'Configuracion de red'
JOIN tecnicos t ON t.correo = 'karla.paredes@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'El router pierde conexion durante la jornada laboral.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'LISTA_PARA_ENTREGA', 'BAJA', DATE_SUB(NOW(), INTERVAL 5 DAY), CURDATE(),
       'Aire acondicionado con bajo rendimiento de enfriamiento.',
       'Servicio terminado, pendiente de entrega.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'LG-AC-006'
JOIN servicios s ON s.nombre = 'Mantenimiento preventivo'
JOIN tecnicos t ON t.correo = 'andrea.lopez@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'Aire acondicionado con bajo rendimiento de enfriamiento.');

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT c.id, e.id, s.id, t.id, 'RECIBIDA', 'MEDIA', NOW(), DATE_ADD(CURDATE(), INTERVAL 4 DAY),
       'Camara de seguridad sin visualizacion remota.',
       'Orden recibida para inspeccion.', NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'HK-CAM-007'
JOIN servicios s ON s.nombre = 'Instalacion y configuracion'
JOIN tecnicos t ON t.correo = 'karla.paredes@nexoservice.com'
WHERE NOT EXISTS (SELECT 1 FROM ordenes_servicio os WHERE os.equipo_id = e.id AND os.descripcion_problema = 'Camara de seguridad sin visualizacion remota.');

INSERT INTO historial_orden
(orden_servicio_id, estado_anterior, estado_nuevo, fecha_cambio, usuario_responsable, observacion)
SELECT os.id, NULL, os.estado, os.fecha_ingreso, 'admin', 'Registro inicial de datos de prueba.'
FROM ordenes_servicio os
WHERE NOT EXISTS (SELECT 1 FROM historial_orden h WHERE h.orden_servicio_id = os.id);

-- =========================================================
-- 7. Diagnosticos, trabajos y evidencias
-- =========================================================

INSERT INTO diagnosticos
(orden_servicio_id, descripcion_falla, causa_probable, solucion_recomendada, observaciones, fecha_diagnostico, usuario_responsable, fecha_creacion)
SELECT os.id,
       'Lentitud general y alto uso de disco al iniciar.',
       'Disco duro mecanico con posible deterioro o saturacion.',
       'Realizar backup, limpieza, optimizacion y evaluar cambio a SSD.',
       'Se recomienda no formatear sin respaldo previo.',
       NOW(), 'tecnico', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'LEN-IP3-002'
WHERE NOT EXISTS (SELECT 1 FROM diagnosticos d WHERE d.orden_servicio_id = os.id);

INSERT INTO diagnosticos
(orden_servicio_id, descripcion_falla, causa_probable, solucion_recomendada, observaciones, fecha_diagnostico, usuario_responsable, fecha_creacion)
SELECT os.id,
       'No enciende y emite pitidos de error.',
       'Posible falla de memoria RAM o placa principal.',
       'Probar memoria, limpiar contactos y validar fuente.',
       'Se requiere aprobacion de cotizacion antes de continuar.',
       NOW(), 'luis.salazar@nexoservice.com', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'HP-PD400-003'
WHERE NOT EXISTS (SELECT 1 FROM diagnosticos d WHERE d.orden_servicio_id = os.id);

INSERT INTO trabajos_orden
(orden_servicio_id, tecnico_id, actividad, detalle, fecha_trabajo, tiempo_invertido_minutos, observaciones, usuario_responsable, fecha_creacion)
SELECT os.id, os.tecnico_id,
       'Revision y diagnostico inicial',
       'Se reviso temperatura, disco, memoria y programas de inicio. Se detecto lentitud por disco mecanico.',
       NOW(), 75, 'Cliente sera informado de recomendaciones.', 'tecnico', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'LEN-IP3-002'
WHERE NOT EXISTS (SELECT 1 FROM trabajos_orden t WHERE t.orden_servicio_id = os.id AND t.actividad = 'Revision y diagnostico inicial');

INSERT INTO trabajos_orden
(orden_servicio_id, tecnico_id, actividad, detalle, fecha_trabajo, tiempo_invertido_minutos, observaciones, usuario_responsable, fecha_creacion)
SELECT os.id, os.tecnico_id,
       'Limpieza y prueba de impresora',
       'Se ejecuto limpieza de cabezales, alineacion y prueba de impresion a color.',
       NOW(), 90, 'Impresora responde correctamente.', 'miguel.rojas@nexoservice.com', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'EPS-L3250-004'
WHERE NOT EXISTS (SELECT 1 FROM trabajos_orden t WHERE t.orden_servicio_id = os.id AND t.actividad = 'Limpieza y prueba de impresora');

INSERT INTO evidencias_orden
(orden_servicio_id, tecnico_id, tipo, nombre_archivo, url_archivo, observaciones, usuario_responsable, fecha_registro)
SELECT os.id, os.tecnico_id, 'FOTO_ANTES', 'laptop-lenovo-antes.jpg', '/uploads/demo/laptop-lenovo-antes.jpg',
       'Estado inicial del equipo antes de limpieza.', 'tecnico', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'LEN-IP3-002'
WHERE NOT EXISTS (SELECT 1 FROM evidencias_orden ev WHERE ev.orden_servicio_id = os.id AND ev.nombre_archivo = 'laptop-lenovo-antes.jpg');

INSERT INTO evidencias_orden
(orden_servicio_id, tecnico_id, tipo, nombre_archivo, url_archivo, observaciones, usuario_responsable, fecha_registro)
SELECT os.id, os.tecnico_id, 'FOTO_DESPUES', 'impresora-epson-despues.jpg', '/uploads/demo/impresora-epson-despues.jpg',
       'Prueba de impresion posterior al mantenimiento.', 'miguel.rojas@nexoservice.com', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'EPS-L3250-004'
WHERE NOT EXISTS (SELECT 1 FROM evidencias_orden ev WHERE ev.orden_servicio_id = os.id AND ev.nombre_archivo = 'impresora-epson-despues.jpg');

-- =========================================================
-- 8. Inventario y movimientos
-- =========================================================

INSERT IGNORE INTO productos_inventario
(codigo, nombre, descripcion, categoria, unidad_medida, stock_actual, stock_minimo, costo_unitario, precio_referencia, activo, fecha_creacion)
VALUES
('REP-SSD-240', 'Disco SSD 240GB', 'SSD SATA para laptops y PCs', 'Almacenamiento', 'UND', 8, 2, 85.00, 130.00, true, NOW()),
('REP-RAM-8DDR4', 'Memoria RAM 8GB DDR4', 'Modulo RAM DDR4 para laptop', 'Memoria', 'UND', 5, 2, 95.00, 150.00, true, NOW()),
('REP-FUENTE-12V', 'Fuente 12V 2A', 'Fuente compatible con routers y equipos de red', 'Energia', 'UND', 1, 2, 18.00, 35.00, true, NOW()),
('INS-PASTA-TERM', 'Pasta termica', 'Pasta termica para mantenimiento preventivo', 'Insumos', 'UND', 12, 3, 8.00, 18.00, true, NOW()),
('INS-LIQ-LIMP', 'Liquido limpiador', 'Liquido de limpieza electronica', 'Insumos', 'UND', 6, 2, 12.00, 25.00, true, NOW());

INSERT INTO movimientos_inventario
(producto_id, orden_servicio_id, tipo_movimiento, cantidad, stock_resultante, motivo, usuario_responsable, fecha_movimiento)
SELECT p.id, NULL, 'ENTRADA', p.stock_actual, p.stock_actual, 'Stock inicial de datos de prueba', 'admin', NOW()
FROM productos_inventario p
WHERE NOT EXISTS (SELECT 1 FROM movimientos_inventario mi WHERE mi.producto_id = p.id AND mi.motivo = 'Stock inicial de datos de prueba');

INSERT INTO movimientos_inventario
(producto_id, orden_servicio_id, tipo_movimiento, cantidad, stock_resultante, motivo, usuario_responsable, fecha_movimiento)
SELECT p.id, os.id, 'SALIDA', 1, p.stock_actual - 1, 'Uso de pasta termica en mantenimiento', 'tecnico', NOW()
FROM productos_inventario p
JOIN ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'LEN-IP3-002'
WHERE p.codigo = 'INS-PASTA-TERM'
  AND NOT EXISTS (SELECT 1 FROM movimientos_inventario mi WHERE mi.producto_id = p.id AND mi.orden_servicio_id = os.id AND mi.motivo = 'Uso de pasta termica en mantenimiento');

-- =========================================================
-- 9. Cotizaciones y pagos
-- =========================================================

INSERT INTO cotizaciones
(numero, orden_servicio_id, fecha_emision, fecha_vencimiento, estado, subtotal, impuesto, total, observaciones, fecha_creacion)
SELECT CONCAT('COT-DEMO-', os.id), os.id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'APROBADA',
       245.00, 0.00, 245.00, 'Cotizacion aprobada por el cliente para continuar el servicio.', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'HP-PD400-003'
WHERE NOT EXISTS (SELECT 1 FROM cotizaciones c WHERE c.numero = CONCAT('COT-DEMO-', os.id));

INSERT INTO cotizacion_detalles
(cotizacion_id, descripcion, cantidad, precio_unitario, subtotal)
SELECT c.id, 'Revision avanzada de placa y memoria RAM', 1, 95.00, 95.00
FROM cotizaciones c
WHERE c.numero LIKE 'COT-DEMO-%'
  AND c.observaciones = 'Cotizacion aprobada por el cliente para continuar el servicio.'
  AND NOT EXISTS (SELECT 1 FROM cotizacion_detalles cd WHERE cd.cotizacion_id = c.id AND cd.descripcion = 'Revision avanzada de placa y memoria RAM');

INSERT INTO cotizacion_detalles
(cotizacion_id, descripcion, cantidad, precio_unitario, subtotal)
SELECT c.id, 'Memoria RAM 8GB DDR4', 1, 150.00, 150.00
FROM cotizaciones c
WHERE c.numero LIKE 'COT-DEMO-%'
  AND c.observaciones = 'Cotizacion aprobada por el cliente para continuar el servicio.'
  AND NOT EXISTS (SELECT 1 FROM cotizacion_detalles cd WHERE cd.cotizacion_id = c.id AND cd.descripcion = 'Memoria RAM 8GB DDR4');

INSERT INTO cotizaciones
(numero, orden_servicio_id, fecha_emision, fecha_vencimiento, estado, subtotal, impuesto, total, observaciones, fecha_creacion)
SELECT CONCAT('COT-PEND-', os.id), os.id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'PENDIENTE',
       90.00, 0.00, 90.00, 'Cotizacion pendiente de aprobacion.', NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id AND e.numero_serie = 'TPL-C6-005'
WHERE NOT EXISTS (SELECT 1 FROM cotizaciones c WHERE c.numero = CONCAT('COT-PEND-', os.id));

INSERT INTO cotizacion_detalles
(cotizacion_id, descripcion, cantidad, precio_unitario, subtotal)
SELECT c.id, 'Cambio de fuente y pruebas de estabilidad', 1, 90.00, 90.00
FROM cotizaciones c
WHERE c.numero LIKE 'COT-PEND-%'
  AND NOT EXISTS (SELECT 1 FROM cotizacion_detalles cd WHERE cd.cotizacion_id = c.id AND cd.descripcion = 'Cambio de fuente y pruebas de estabilidad');

INSERT INTO pagos
(orden_servicio_id, cotizacion_id, monto, metodo_pago, estado, numero_operacion, observaciones, usuario_responsable, fecha_pago, fecha_creacion)
SELECT c.orden_servicio_id, c.id, 100.00, 'YAPE', 'REGISTRADO', 'YP-DEMO-1001',
       'Pago parcial registrado para prueba.', 'recepcion', NOW(), NOW()
FROM cotizaciones c
WHERE c.estado = 'APROBADA'
  AND c.numero LIKE 'COT-DEMO-%'
  AND NOT EXISTS (SELECT 1 FROM pagos p WHERE p.numero_operacion = 'YP-DEMO-1001');

INSERT INTO pagos
(orden_servicio_id, cotizacion_id, monto, metodo_pago, estado, numero_operacion, observaciones, usuario_responsable, fecha_pago, fecha_creacion)
SELECT c.orden_servicio_id, c.id, 145.00, 'TRANSFERENCIA', 'REGISTRADO', 'TR-DEMO-1002',
       'Pago final registrado para prueba.', 'recepcion', NOW(), NOW()
FROM cotizaciones c
WHERE c.estado = 'APROBADA'
  AND c.numero LIKE 'COT-DEMO-%'
  AND NOT EXISTS (SELECT 1 FROM pagos p WHERE p.numero_operacion = 'TR-DEMO-1002');

-- =========================================================
-- 10. Mantenimientos programados
-- =========================================================

INSERT INTO mantenimientos_programados
(equipo_id, servicio_id, frecuencia, estado, fecha_inicio, proxima_fecha, ultima_orden_id, observaciones, fecha_creacion)
SELECT e.id, s.id, 'TRIMESTRAL', 'ACTIVO', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), NULL,
       'Plan preventivo trimestral para activo critico.', NOW()
FROM equipos e
JOIN servicios s ON s.nombre = 'Mantenimiento preventivo'
WHERE e.numero_serie = 'LG-AC-006'
  AND NOT EXISTS (SELECT 1 FROM mantenimientos_programados mp WHERE mp.equipo_id = e.id AND mp.servicio_id = s.id);

INSERT INTO mantenimientos_programados
(equipo_id, servicio_id, frecuencia, estado, fecha_inicio, proxima_fecha, ultima_orden_id, observaciones, fecha_creacion)
SELECT e.id, s.id, 'MENSUAL', 'ACTIVO', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 DAY), NULL,
       'Revision mensual de conectividad y grabacion.', NOW()
FROM equipos e
JOIN servicios s ON s.nombre = 'Instalacion y configuracion'
WHERE e.numero_serie = 'HK-CAM-007'
  AND NOT EXISTS (SELECT 1 FROM mantenimientos_programados mp WHERE mp.equipo_id = e.id AND mp.servicio_id = s.id);

-- =========================================================
-- 11. Resumen rapido
-- =========================================================

SELECT 'Datos de prueba completos cargados correctamente' AS mensaje;
