USE nexoservice_db;

-- Datos de prueba para NexoService 360.
-- Ejecuta primero la aplicacion una vez para que Hibernate cree las tablas.
-- Los roles y usuarios base se crean automaticamente desde DataInitializer.

INSERT IGNORE INTO clientes
(nombres, apellidos, tipo_documento, numero_documento, correo, telefono, direccion, activo, fecha_creacion)
VALUES
('Abraham', 'Tintaya', 'DNI', '76543210', 'abraham.tintaya@gmail.com', '987654321', 'Av. Los Proceres 123, Lima', true, NOW()),
('Maria', 'Quispe Ramos', 'DNI', '70123456', 'maria.quispe@gmail.com', '956123789', 'Jr. Las Flores 450, San Juan de Lurigancho', true, NOW()),
('Carlos', 'Mamani Torres', 'DNI', '73456789', 'carlos.mamani@gmail.com', '945678123', 'Av. Universitaria 220, Los Olivos', true, NOW()),
('TecnoMarket Peru', NULL, 'RUC', '20604567891', 'contacto@tecnomarket.pe', '014567890', 'Av. Arequipa 1500, Lince', true, NOW());

INSERT IGNORE INTO tecnicos
(nombres, apellidos, especialidad, correo, telefono, disponibilidad, activo, fecha_creacion)
VALUES
('Luis', 'Salazar Vega', 'Soporte de laptops y PCs', 'luis.salazar@nexoservice.com', '955111222', 'DISPONIBLE', true, NOW()),
('Karla', 'Paredes Soto', 'Redes y mantenimiento preventivo', 'karla.paredes@nexoservice.com', '955333444', 'DISPONIBLE', true, NOW()),
('Miguel', 'Rojas Flores', 'Impresoras y perifericos', 'miguel.rojas@nexoservice.com', '955555666', 'OCUPADO', true, NOW()),
('Tecnico', 'Demo', 'Diagnostico y reparacion', 'tecnico@nexoservice.com', '955777888', 'DISPONIBLE', true, NOW());

INSERT IGNORE INTO servicios
(nombre, descripcion, precio_base, duracion_estimada_minutos, activo, fecha_creacion)
VALUES
('Diagnostico tecnico', 'Revision inicial del equipo y deteccion de falla.', 35.00, 45, true, NOW()),
('Mantenimiento preventivo', 'Limpieza fisica, revision de temperatura y optimizacion basica.', 80.00, 120, true, NOW()),
('Formateo e instalacion de sistema', 'Instalacion de sistema operativo, drivers y programas basicos.', 120.00, 180, true, NOW()),
('Cambio de disco SSD', 'Instalacion de SSD y migracion o reinstalacion del sistema.', 160.00, 150, true, NOW()),
('Configuracion de red', 'Configuracion de router, impresora compartida o red local.', 90.00, 90, true, NOW());

INSERT IGNORE INTO equipos
(cliente_id, tipo, marca, modelo, numero_serie, descripcion, estado, fecha_creacion)
VALUES
((SELECT id FROM clientes WHERE tipo_documento = 'DNI' AND numero_documento = '76543210'), 'Laptop', 'ASUS', 'VivoBook 15', 'ASUS-VB15-001', 'Laptop personal color gris, cargador incluido.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE tipo_documento = 'DNI' AND numero_documento = '70123456'), 'Laptop', 'Lenovo', 'IdeaPad 3', 'LEN-IP3-002', 'Laptop con lentitud al iniciar Windows.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE tipo_documento = 'DNI' AND numero_documento = '73456789'), 'PC Escritorio', 'HP', 'ProDesk 400', 'HP-PD400-003', 'CPU de oficina, no enciende correctamente.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE tipo_documento = 'RUC' AND numero_documento = '20604567891'), 'Impresora', 'Epson', 'L3250', 'EPS-L3250-004', 'Impresora multifuncional con error de impresion.', 'ACTIVO', NOW()),
((SELECT id FROM clientes WHERE tipo_documento = 'RUC' AND numero_documento = '20604567891'), 'Router', 'TP-Link', 'Archer C6', 'TPL-C6-005', 'Router principal de oficina.', 'ACTIVO', NOW());

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT
    c.id,
    e.id,
    s.id,
    t.id,
    'ASIGNADA',
    'ALTA',
    NOW(),
    DATE_ADD(CURDATE(), INTERVAL 2 DAY),
    'La laptop se apaga despues de unos minutos de uso.',
    'Cliente indica que el problema ocurre cuando abre varias aplicaciones.',
    NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'ASUS-VB15-001'
JOIN servicios s ON s.nombre = 'Diagnostico tecnico'
JOIN tecnicos t ON t.correo = 'luis.salazar@nexoservice.com'
WHERE c.tipo_documento = 'DNI'
  AND c.numero_documento = '76543210'
  AND NOT EXISTS (
      SELECT 1 FROM ordenes_servicio os
      WHERE os.equipo_id = e.id
        AND os.descripcion_problema = 'La laptop se apaga despues de unos minutos de uso.'
  );

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT
    c.id,
    e.id,
    s.id,
    t.id,
    'EN_DIAGNOSTICO',
    'MEDIA',
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    DATE_ADD(CURDATE(), INTERVAL 1 DAY),
    'El equipo esta muy lento y demora en iniciar.',
    'Se solicita revisar disco, memoria y programas de inicio.',
    NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'LEN-IP3-002'
JOIN servicios s ON s.nombre = 'Mantenimiento preventivo'
JOIN tecnicos t ON t.correo = 'tecnico@nexoservice.com'
WHERE c.tipo_documento = 'DNI'
  AND c.numero_documento = '70123456'
  AND NOT EXISTS (
      SELECT 1 FROM ordenes_servicio os
      WHERE os.equipo_id = e.id
        AND os.descripcion_problema = 'El equipo esta muy lento y demora en iniciar.'
  );

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT
    c.id,
    e.id,
    s.id,
    t.id,
    'RECIBIDA',
    'URGENTE',
    NOW(),
    DATE_ADD(CURDATE(), INTERVAL 3 DAY),
    'La PC no enciende y emite pitidos al presionar el boton de encendido.',
    'Equipo recibido sin monitor ni teclado.',
    NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'HP-PD400-003'
JOIN servicios s ON s.nombre = 'Diagnostico tecnico'
JOIN tecnicos t ON t.correo = 'luis.salazar@nexoservice.com'
WHERE c.tipo_documento = 'DNI'
  AND c.numero_documento = '73456789'
  AND NOT EXISTS (
      SELECT 1 FROM ordenes_servicio os
      WHERE os.equipo_id = e.id
        AND os.descripcion_problema = 'La PC no enciende y emite pitidos al presionar el boton de encendido.'
  );

INSERT INTO ordenes_servicio
(cliente_id, equipo_id, servicio_id, tecnico_id, estado, prioridad, fecha_ingreso, fecha_estimada, descripcion_problema, observaciones, fecha_creacion)
SELECT
    c.id,
    e.id,
    s.id,
    t.id,
    'FINALIZADA',
    'BAJA',
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    CURDATE(),
    'La impresora no imprime en color y muestra alerta de mantenimiento.',
    'Servicio terminado, pendiente de entrega al cliente.',
    NOW()
FROM clientes c
JOIN equipos e ON e.cliente_id = c.id AND e.numero_serie = 'EPS-L3250-004'
JOIN servicios s ON s.nombre = 'Diagnostico tecnico'
JOIN tecnicos t ON t.correo = 'miguel.rojas@nexoservice.com'
WHERE c.tipo_documento = 'RUC'
  AND c.numero_documento = '20604567891'
  AND NOT EXISTS (
      SELECT 1 FROM ordenes_servicio os
      WHERE os.equipo_id = e.id
        AND os.descripcion_problema = 'La impresora no imprime en color y muestra alerta de mantenimiento.'
  );

INSERT INTO historial_orden
(orden_servicio_id, estado_anterior, estado_nuevo, fecha_cambio, usuario_responsable, observacion)
SELECT os.id, NULL, os.estado, os.fecha_ingreso, 'admin', 'Registro inicial de datos de prueba.'
FROM ordenes_servicio os
WHERE NOT EXISTS (
    SELECT 1 FROM historial_orden h
    WHERE h.orden_servicio_id = os.id
);

INSERT INTO diagnosticos
(orden_servicio_id, descripcion_falla, causa_probable, solucion_recomendada, observaciones, fecha_diagnostico, usuario_responsable, fecha_creacion)
SELECT
    os.id,
    'El equipo presenta lentitud general y uso alto de disco al iniciar.',
    'Posible saturacion de programas de inicio o disco duro deteriorado.',
    'Realizar mantenimiento preventivo, revisar estado SMART del disco y optimizar inicio.',
    'Se recomienda backup antes de cualquier reparacion mayor.',
    NOW(),
    'karla.paredes@nexoservice.com',
    NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id
WHERE e.numero_serie = 'LEN-IP3-002'
  AND NOT EXISTS (
      SELECT 1 FROM diagnosticos d
      WHERE d.orden_servicio_id = os.id
  );

INSERT INTO diagnosticos
(orden_servicio_id, descripcion_falla, causa_probable, solucion_recomendada, observaciones, fecha_diagnostico, usuario_responsable, fecha_creacion)
SELECT
    os.id,
    'La impresora no imprime correctamente en color.',
    'Inyectores parcialmente obstruidos por falta de uso.',
    'Ejecutar limpieza de cabezales y prueba de impresion.',
    'Equipo listo para entrega luego de pruebas satisfactorias.',
    NOW(),
    'miguel.rojas@nexoservice.com',
    NOW()
FROM ordenes_servicio os
JOIN equipos e ON e.id = os.equipo_id
WHERE e.numero_serie = 'EPS-L3250-004'
  AND NOT EXISTS (
      SELECT 1 FROM diagnosticos d
      WHERE d.orden_servicio_id = os.id
  );
