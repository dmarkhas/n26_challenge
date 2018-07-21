# N26 Code Challenge

## Project Build

The project is based on Sprint Boot, using Gradle as a build tool. It can be built with `gradle build` and started by running `gradle bootRun`.

## Project Dependencies

The project was created with Sprint Initializr, and depends on the following components:

- Sprint Boot is mainly responsible for the web server implementation (Tomcat, routing, exception handling, property binding, etc.)
- Mapstruct mapper for mapping DTOs to POJOs
- Swagger for generating API docs and a Swagger UI (accessible at /swagger-ui.html)
- Lombok for generating boilerplate code (Getter/Setters/Constructors/hashCode/equalsTo)

## Transactions Handling

All valid (with timestamp > min. allowed time, defaults to 60 seconds) transactions are aggregated in buckets, where each bucket contains all transactions that occurred at the same second (between .000 and .999 milliseconds).

The aggregated statistics are stored in a ConcurrentHashMap which ensures thread safety, with a background thread that trims the map by removing buckets that have expired.

This allows both adding and retrieving statistics to happen in O(1) complexity, with the upper limit being the # of seconds the data is kept.
