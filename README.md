# N26 Code Challenge

## Project Build

The project is based on Sprint Boot, using Gradle as a build tool. It can be built with `gradle build` and started by running `gradle bootRun`.

## Project Dependencies

The project was created with Sprint Initializr, and depends on the following components:

- Sprint Boot is mainly responsible for the web server implementation (Tomcat, routing, exception handling, property binding, etc.)
- Mapstruct mapper for mapping DTOs to POJOs
- Swagger for generating API docs and a Swagger UI (accessible at /swagger-ui.html)
- Lombok for generating boilerplate code (Getter/Setters/Constructors/hashCode/equalsTo)
