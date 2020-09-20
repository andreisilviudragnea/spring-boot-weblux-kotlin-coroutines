# spring-boot-weblux-kotllin-coroutines
Spring Boot + WebFlux + Kotlin coroutines example project

Technology stack includes:
- Java 14
- Gradle build system with Kotlin DSL build script
- Spring Boot
- Spring WebFlux
- Kotlin coroutines
- Spring Data Reactive Redis
- Spring R2DBC (Reactive RDBC) with H2 database example
- Docker image deployment

There are 3 versions of each API:
- Reactive-based controllers (`/v1`)
- Coroutine-based controllers (`/v2`)
- Functional endpoints (`/v3`)
