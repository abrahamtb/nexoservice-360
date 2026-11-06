# Revisión de seguridad pendiente de validación

Este documento describe una propuesta de endurecimiento para el entorno **académico** de NexoService 360. No equivale a una auditoría exhaustiva.

## Cambios propuestos en esta rama

1. La contraseña de MySQL ya no tiene un valor de respaldo en `application.properties`; debe proporcionarse mediante `DB_PASSWORD`.
2. La generación de usuarios demostrativos se desactiva por defecto. Solo se activa de manera expresa mediante la propiedad `app.demo-users.enabled=true`.

## Configuración local

Antes de arrancar el proyecto, establece las variables necesarias (no uses credenciales de producción):

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/nexoservice_db?useSSL=false&serverTimezone=America/Lima"
$env:DB_USERNAME="usuario_local"
$env:DB_PASSWORD="contraseña_local_segura"
mvn spring-boot:run
```

Para usar cuentas de demostración, solo en una base de pruebas local y nunca en un servidor público:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--app.demo-users.enabled=true"
```

**Atención:** la propiedad solo controla la creación automática de las cuentas. No elimina usuarios existentes, ni actualiza sus contraseñas.

## Riesgos pendientes

- Los secretos antiguos podrían permanecer en el historial de Git. Si se usaron fuera de pruebas, **róta las credenciales**; borrar la línea actual no las invalida.
- Las claves de ejemplo del inicializador siguen siendo predecibles. Reemplazarlas por un mecanismo de aprovisionamiento seguro antes de cualquier despliegue.
- `SecurityConfig` desactiva CSRF. Revisar y probar CSRF para sesiones de navegador y operaciones mutables antes de exponer el sistema en Internet.
- Revisar el perfil activo por defecto y el acceso de demostración a la base de datos.
- Ejecutar pruebas de inicio, login, permisos y CRUD con y sin `app.demo-users.enabled` antes de fusionar.

## Estado de validación

**Pendiente:** estos cambios se revisaron estáticamente, pero no se ejecutaron pruebas en entorno Java/MySQL desde esta conversación.
