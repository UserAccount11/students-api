# API CRUD de Estudiantes - Arquitectura Hexagonal con Spring Boot 3

Este proyecto implementa una API para gestionar las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre un modelo de `Student` (Estudiante) utilizando la arquitectura hexagonal (también conocida como arquitectura de puertos y adaptadores). La API está desarrollada con **Spring Boot 3** y se integra con una API externa para el manejo de matrículas.

## Características

- **CRUD completo**: Permite realizar operaciones para crear, leer, actualizar y eliminar estudiantes.
- **Arquitectura Hexagonal**: Organiza el código separando las capas de dominio, aplicación y puertos/adaptadores, facilitando el mantenimiento y escalabilidad.
- **Spring Boot 3**: Utiliza las últimas características de Spring para un desarrollo ágil y eficiente.
- **Interacción con API de Matrículas**: La API se comunica con una API externa para gestionar la matriculación de los estudiantes.
- **Interacción con Resource Server**: La API se comunica con una servidor de autorización para securizar los endpoints.

## Requisitos

- Java 17+
- Maven 3+
- Spring Boot 3.0+
- Base de datos (MySQL, PostgreSQL, etc.)
- API externa de matrículas (Se debe configurar el endpoint en el archivo de configuración)

## Estructura del Proyecto

El proyecto sigue la arquitectura hexagonal, dividida en las siguientes capas:

1. **Dominio**: Contiene las entidades y las reglas de negocio. 
   - `Student`: La entidad principal del modelo de dominio.
   
2. **Aplicación**: Contiene los casos de uso, los servicios que gestionan la lógica del sistema.
   - Casos de uso como `CrearEstudiante`, `ActualizarEstudiante`, `EliminarEstudiante`, `ObtenerEstudiante`.
   
3. **Infraestructura**: Contiene los adaptadores y los puertos. Aquí se encuentran los controladores REST, los repositorios y la integración con la API externa de matrículas.
   - **API REST**: Controladores para gestionar las solicitudes HTTP.
   - **API de Matrículas**: Adaptador para interactuar con la API externa de matriculaciones.

## Endpoints

| Método | Endpoint         | Descripción                        | Cuerpo (Body)                               |
|--------|------------------|------------------------------------|---------------------------------------------|
| GET    | `/students`      | Obtener todos los estudiantes      | N/A                                         |
| GET    | `/students/{id}` | Obtener un estudiante por ID       | N/A                                         |
| POST   | `/students`      | Crear un nuevo estudiante          | `{ "name": "Nombre", "age": 20, ... }`      |
| PUT    | `/students/{id}` | Actualizar un estudiante existente | `{ "name": "NuevoNombre", "age": 21, ... }` |
| DELETE | `/students/{id}` | Eliminar un estudiante por ID      | N/A                                         |

### Integración con API de Matrículas

- La API de estudiantes interactúa con la API externa de matrículas para gestionar las inscripciones de los estudiantes en cursos. Los detalles de integración (endpoints de la API externa y autenticación) se configuran en el archivo `application.yml`.

## Instalación

1. Clonar el repositorio:

    ```bash
    git clone https://github.com/tu_usuario/tu_repositorio.git
    ```

2. Acceder al directorio del proyecto:

    ```bash
    cd tu_repositorio
    ```

3. Configurar las propiedades en `src/main/resources/application.yml`:

    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/mi_base_datos
        username: usuario
        password: contraseña
      jpa:
        hibernate:
          ddl-auto: update
    
    api.matriculas:
      url: https://api.matriculas.com/v1
      token: tu_token_de_acceso
    ```

4. Compilar y ejecutar la aplicación:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Testing

Para ejecutar los tests unitarios y de integración:

```bash
mvn test
