# Portal de Capacitaciones - Collections de Postman

Este directorio contiene las colecciones de Postman para probar el servicio de cursos del Portal de Capacitaciones.

## 📁 Archivos Incluidos

- **Portal-Capacitaciones-Courses.postman_collection.json** - Colección principal con todos los endpoints
- **Portal-Capacitaciones-Environment.postman_environment.json** - Variables de entorno
- **README.md** - Este archivo con instrucciones

## 🚀 Configuración Inicial

### 1. Importar en Postman

1. Abre Postman
2. Haz clic en **Import**
3. Selecciona los archivos:
   - `Portal-Capacitaciones-Courses.postman_collection.json`
   - `Portal-Capacitaciones-Environment.postman_environment.json`

### 2. Configurar Variables de Entorno

1. En Postman, ve a **Environments**
2. Selecciona **Portal Capacitaciones - Courses Service Environment**
3. Verifica que las variables estén configuradas:
   - `base_url`: `http://localhost:8081/courses-service`
   - `programa_id`: `1`
   - `unidad_id`: `1`
   - `leccion_id`: `1`

## 📋 Flujo de Pruebas Recomendado

### Paso 1: Inicializar Datos de Prueba
```
POST /api/datos-prueba/inicializar
```
- Ejecuta este endpoint primero para poblar la base de datos con datos de prueba
- Esto creará 4 programas completos con unidades y lecciones

### Paso 2: Explorar Programas
```
GET /api/programas
GET /api/programas/1
GET /api/programas/1/completo
```

### Paso 3: Explorar Unidades
```
GET /api/unidades/programa/1
GET /api/unidades/1
GET /api/unidades/1/completo
```

### Paso 4: Explorar Lecciones
```
GET /api/lecciones/unidad/1
GET /api/lecciones/programa/1
GET /api/lecciones/1
```

### Paso 5: Probar Operaciones CRUD
- Crear nuevos programas, unidades y lecciones
- Actualizar registros existentes
- Eliminar registros (opcional)

## 🔧 Endpoints Disponibles

### 📚 Programas
- `POST /api/programas` - Crear programa
- `GET /api/programas` - Listar todos los programas
- `GET /api/programas/{id}` - Obtener programa por ID
- `GET /api/programas/{id}/completo` - Programa con unidades y lecciones
- `PUT /api/programas/{id}` - Actualizar programa
- `DELETE /api/programas/{id}` - Eliminar programa
- `GET /api/programas/buscar/area?area=` - Buscar por área
- `GET /api/programas/buscar/titulo?titulo=` - Buscar por título
- `GET /api/programas/recientes` - Programas recientes
- `GET /api/programas/estadisticas/areas` - Estadísticas por área

### 📖 Unidades
- `POST /api/unidades` - Crear unidad
- `GET /api/unidades/programa/{programaId}` - Unidades de un programa
- `GET /api/unidades/{id}` - Obtener unidad por ID
- `GET /api/unidades/{id}/completo` - Unidad con lecciones
- `PUT /api/unidades/{id}` - Actualizar unidad
- `DELETE /api/unidades/{id}` - Eliminar unidad
- `GET /api/unidades/buscar/titulo?titulo=` - Buscar por título
- `GET /api/unidades/programa/{programaId}/contar` - Contar unidades
- `PUT /api/unidades/programa/{programaId}/reordenar` - Reordenar unidades

### 📝 Lecciones
- `POST /api/lecciones` - Crear lección
- `GET /api/lecciones/unidad/{unidadId}` - Lecciones de una unidad
- `GET /api/lecciones/programa/{programaId}` - Lecciones de un programa
- `GET /api/lecciones/{id}` - Obtener lección por ID
- `GET /api/lecciones/{id}/completo` - Lección completa
- `PUT /api/lecciones/{id}` - Actualizar lección
- `DELETE /api/lecciones/{id}` - Eliminar lección
- `GET /api/lecciones/buscar/tipo?tipo=` - Buscar por tipo (video, pdf, link)
- `GET /api/lecciones/buscar/titulo?titulo=` - Buscar por título
- `GET /api/lecciones/unidad/{unidadId}/contar` - Contar lecciones
- `GET /api/lecciones/estadisticas/tipo?tipo=` - Estadísticas por tipo
- `PUT /api/lecciones/unidad/{unidadId}/reordenar` - Reordenar lecciones

## 📊 Datos de Prueba Incluidos

Al ejecutar `/api/datos-prueba/inicializar`, se crean:

### 1. Desarrollo Fullstack con Angular y Spring Boot
- **Unidad 1**: Fundamentos de Angular
  - Introducción a Angular (video)
  - Componentes y Directivas (pdf)
- **Unidad 2**: Spring Boot Backend
  - Configuración de Spring Boot (video)
  - REST APIs con Spring (link)

### 2. APIs e Integraciones - DataPower y IBM Bus
- **Unidad 1**: IBM DataPower Gateway
  - Introducción a DataPower (video)
  - Configuración de Políticas (pdf)
- **Unidad 2**: IBM Integration Bus
  - Arquitectura de IBM Bus (video)

### 3. Cloud Computing - AWS y Azure
- **Unidad 1**: Amazon Web Services
  - Fundamentos de AWS (video)
  - EC2 y S3 (link)

### 4. Data Engineering con Python y Spark
- **Unidad 1**: Python para Data Science
  - Pandas y NumPy (video)
  - Visualización con Matplotlib (pdf)
- **Unidad 2**: Apache Spark
  - Introducción a Spark (video)

## 🎯 Tipos de Material Soportados

- **video**: Enlaces a videos (YouTube, Vimeo, etc.)
- **pdf**: Enlaces a documentos PDF
- **link**: Enlaces a recursos web externos

## ⚠️ Notas Importantes

1. **Base de Datos**: Asegúrate de que PostgreSQL esté ejecutándose
2. **Esquema**: El esquema `capacitaciones` debe existir en la base de datos
3. **Puerto**: El servicio se ejecuta en el puerto 8081
4. **CORS**: Está habilitado para permitir peticiones desde el frontend

## 🔍 Troubleshooting

### Error de Conexión
- Verifica que el servicio esté ejecutándose en `http://localhost:8081`
- Revisa que PostgreSQL esté disponible

### Error 404
- Verifica que el endpoint esté correctamente escrito
- Asegúrate de que el servicio esté completamente iniciado

### Error 500
- Revisa los logs del servicio
- Verifica que la base de datos esté configurada correctamente

## 📞 Soporte

Para problemas o dudas sobre el servicio de cursos, revisa:
1. Los logs de la aplicación
2. La configuración de la base de datos
3. La documentación de la API


