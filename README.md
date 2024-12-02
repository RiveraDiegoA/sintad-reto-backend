# Proyecto Backend REST con Spring Boot

Este proyecto es una API REST desarrollada con **Spring Boot** que gestiona entidades relacionadas con contribuyentes, documentos y usuarios. El proyecto incluye autenticación basada en **JWT**, documentación con **Swagger**, y un manejo de errores robusto. A continuación se presenta una descripción detallada de la estructura y funcionalidades del proyecto.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.0.5**
- **Spring Data JPA**: Para la persistencia de datos.
- **Spring Security**: Para la autenticación y autorización de usuarios.
- **JWT**: Autenticación sin estado (stateless).
- **Hibernate Validator**: Validación de entradas.
- **H2**: Base de datos embebida para desarrollo y pruebas.
- **Lombok**: Reducir el boilerplate de código.
- **Swagger/OpenAPI**: Documentación de la API.
- **Maven**: Para la gestión de dependencias.

## Estructura del Proyecto

La estructura del proyecto sigue la arquitectura típica en capas (Controller - Service - Repository) utilizada en proyectos de **Spring Boot**. A continuación se detalla cada uno de los paquetes y sus responsabilidades.

### Diagrama de la Estructura del Proyecto

```
src/main/java/com/rivera/springboot/backend/apirest/challenge
    └── config
        └── AdminInitializer.java
        └── SecurityConfig.java
        └── auth
            └── CustomUserDetailsService.java
    └── controller
        └── AuthController.java
        └── EntidadController.java
        └── TipoContribuyenteController.java
        └── TipoDocumentoController.java
        └── UsuarioController.java
    └── dto
        └── AuthDTO.java
        └── EntidadDTO.java
        └── TipoContribuyenteDTO.java
        └── TipoDocumentoDTO.java
        └── UsuarioDTO.java
    └── entity
        └── Entidad.java
        └── TipoContribuyente.java
        └── TipoDocumento.java
        └── Usuario.java
    └── exception
        └── ArgumentNotValidException.java
        └── DuplicateRecordOrBadRequestException.java
        └── GlobalExceptionHandler.java
        └── ResourceNotFoundException.java
    └── repository
        └── EntidadRepository.java
        └── TipoContribuyenteRepository.java
        └── TipoDocumentoRepository.java
        └── UsuarioRepository.java
    └── service
        └── EntidadService.java
        └── TipoContribuyenteService.java
        └── TipoDocumentoService.java
        └── UsuarioService.java
    └── util
        └── JwtRequestFilter.java
        └── JwtUtil.java

src/main/resources
    └── import.sql
```

### 1. `entity`
Contiene las clases que representan las entidades del sistema y están anotadas con **JPA** para el mapeo relacional:
- **`TipoContribuyente`**: Define el tipo de contribuyente, como personas naturales o jurídicas.
- **`TipoDocumento`**: Representa los diferentes tipos de documentos.
- **`Usuario`**: Maneja la información del usuario y el rol asociado.
- **`Entidad`**: Agrupa la información de una entidad, incluyendo el tipo de documento y tipo de contribuyente.

### 2. `repository`
Interfaz que extiende **`JpaRepository`** para cada una de las entidades. Proporciona acceso a las operaciones CRUD y consultas personalizadas:
- **`TipoContribuyenteRepository`**
- **`TipoDocumentoRepository`**
- **`UsuarioRepository`**
- **`EntidadRepository`**

### 3. `service`
Contiene la lógica de negocio para manejar las operaciones del sistema. Cada servicio utiliza un repositorio correspondiente para realizar tareas como:
- Listar entidades
- Filtrar por estado
- Crear, actualizar y eliminar registros

### 4. `controller`
Define los **endpoints** REST para interactuar con cada entidad del sistema. Algunos controladores destacados son:
- **`EntidadController`**: Proporciona operaciones para gestionar las entidades, como creación, edición, eliminación, etc.
- **`TipoDocumentoController`**, **`TipoContribuyenteController`**, **`UsuarioController`**: Expone las APIs para realizar operaciones CRUD de cada una de las entidades.
- **`AuthController`**: Controlador para la autenticación y generación de tokens JWT.

### 5. `config`
Define la configuración de seguridad y los componentes relacionados:
- **`SecurityConfig`**: Configura la seguridad HTTP y los filtros de JWT para proteger las APIs.
- **`AdminInitializer`**: Inicializa dos usuarios administradores (`admin1` y `admin2`) al inicio de la aplicación.

### 6. `dto`
Contiene los **Data Transfer Objects (DTO)** para facilitar la transferencia de datos entre cliente y servidor, con el fin de aplicar validaciones y simplificar las respuestas.
- **`AuthDTO`**: DTO para la autenticación de usuarios.
- **`EntidadDTO`, `TipoContribuyenteDTO`, `TipoDocumentoDTO`, `UsuarioDTO`**: DTOs utilizados en los endpoints de cada entidad para validar y manejar las solicitudes de datos.

### 7. `exception`
Define clases para el manejo de excepciones personalizadas y un **manejador global** para capturar errores de manera uniforme:
- **`GlobalExceptionHandler`**: Maneja excepciones como `ResourceNotFoundException`, `ArgumentNotValidException`, y errores generales del servidor.
- **`DuplicateRecordOrBadRequestException`, `ResourceNotFoundException`, `ArgumentNotValidException`**: Excepciones personalizadas que ayudan a manejar situaciones específicas como duplicidad de registros o errores de validación.

### 8. `util`
- **`JwtRequestFilter`**: Filtro para validar los tokens JWT en cada solicitud protegida.
- **`JwtUtil`**: Clase utilitaria para generar y validar los tokens JWT.

## Seguridad y Autenticación

La seguridad del proyecto se implementa usando **Spring Security** y **JWT**:
- Solo los endpoints de autenticación (`/api/v1/auth/login`) están abiertos. Todos los demás endpoints requieren un token JWT válido.
- **`JwtRequestFilter`** se encarga de validar el token en cada solicitud.

Se incluyen dos administradores (`admin1` y `admin2`) generados automáticamente al iniciar la aplicación mediante el componente **`AdminInitializer`**.

## Instalación y Ejecución

### Prerrequisitos
- **Java 17**
- **Maven**

### Pasos para Ejecutar
1. Clona el repositorio:
   ```sh
   git clone <url-del-repositorio>
   cd nombre-del-repositorio
   ```
2. Compila y ejecuta el proyecto usando Maven:
   ```sh
   mvn spring-boot:run
   ```
3. La aplicación estará disponible en: `http://localhost:8080`

### Importar Datos Iniciales
El archivo `import.sql` contiene datos iniciales que serán insertados automáticamente al levantar la aplicación. Estos datos incluyen registros básicos para realizar pruebas.

## Endpoints Principales

A continuación se presenta un resumen de los endpoints disponibles. La documentación detallada se puede consultar a través de Swagger en `http://localhost:8080/swagger-ui.html`.

- **Entidad**:
  - `GET /api/v1/entidad`: Lista todas las entidades.
  - `POST /api/v1/entidad`: Registra una nueva entidad.
  - `PUT /api/v1/entidad/{id}`: Actualiza una entidad.
  - `DELETE /api/v1/entidad/{id}`: Elimina una entidad.

- **Tipo Documento**:
  - `GET /api/v1/tipo-documento`: Lista todos los tipos de documentos.
  - `POST /api/v1/tipo-documento`: Registra un nuevo tipo de documento.

- **Usuario**:
  - `GET /api/v1/usuario`: Lista todos los usuarios.
  - `POST /api/v1/usuario`: Crea un nuevo usuario.

- **Autenticación**:
  - `POST /api/v1/auth/login`: Iniciar sesión y obtener un token JWT.

## Swagger
La documentación de la API está disponible utilizando **Swagger**. Esto facilita la exploración y prueba de los endpoints.

Para acceder a la documentación, dirígete a: [Swagger UI](http://localhost:8080/swagger-ui.html).

## Contribuir
Las contribuciones son bienvenidas. Si deseas colaborar, por favor sigue los siguientes pasos:
1. Haz un fork del proyecto.
2. Crea una rama para tu nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza los cambios necesarios y haz un commit (`git commit -m 'Agregar nueva funcionalidad'`).
4. Sube tus cambios (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.

## Licencia
Este proyecto está licenciado bajo la **MIT License** - consulta el archivo `LICENSE` para más detalles.

## Contacto
Para consultas o sugerencias, puedes contactarme a través de:
- **Email**: diegorivera.a04@gmail.com
- **LinkedIn**: https://www.linkedin.com/in/diego-rivera-ambrosio

---
Este proyecto fue desarrollado para demostrar buenas prácticas de desarrollo de APIs REST utilizando Spring Boot y otras tecnologías modernas. ¡Espero que lo encuentres útil! 😊