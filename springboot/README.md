[![Spring Boot CI with Gradle](https://github.com/srinivasa-vasu/yb-ms-data/actions/workflows/gradle-boot.yml/badge.svg?branch=main)](https://github.com/srinivasa-vasu/yb-ms-data/actions/workflows/gradle-boot.yml)

[Spring Boot](https://spring.io/projects/spring-boot) makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

This describes how to build a simple JPA based web application using Spring Boot framework for YSQL API using [Yugabyte JDBC Driver](https://docs.yugabyte.com/latest/integrations/jdbc-driver/).

## Prerequisites

- Follow [YB Quick start](https://docs.yugabyte.com/latest/quick-start/) instructions to run a local YugabyteDB cluster. Test YugabyteDB's YSQL API, as [documented](../../quick-start/explore/ysql/) so that you can confirm that you have YSQL service running on `localhost:5433`.
- You will need JDK 17 or later. You can use [SDKMAN](https://sdkman.io/install) to install the JDK runtime.

## Get Started

You can find the complete source at [java framework with smart driver for YSQL](https://github.com/yugabyte/yb-ms-data.git). This project has directories for different java frameworks such as spring-boot, quarkus and micronaut. Clone this repository to a local workstation and open the `yb-ms-data` directory in your favorite IDE to easily navigate and explore Spring Boot's project files.

```sh
git clone https://github.com/srinivasa-vasu/yb-ms-data.git
```

## Dependencies

This project depends on the following libraries.
```gradle
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.retry:spring-retry")
    implementation("com.yugabyte:jdbc-yugabytedb:42.3.5-yb-1")
    implementation("org.postgresql:postgresql:42.5.1")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:7.0.0")
    testImplementation("org.testcontainers:yugabytedb")
    testImplementation("org.testcontainers:junit-jupiter")
```
Update the driver dependency library **("com.yugabyte:jdbc-yugabytedb")** to the latest version. Grab the latest version from [Yugabyte JDBC driver](https://docs.yugabyte.com/latest/integrations/jdbc-driver/).

## Driver Configuration

Refer to the file `yb-ms-data/springboot/src/main/resources/application.yaml` in the project directory:

```yml
spring:
  jpa:
    properties:
      hibernate:
        connection:
          provider_disables_autocommit: true
        default_schema: todo
    open-in-view: false
  datasource:
    url: jdbc:yugabytedb://[hostname:port]/yugabyte?load-balance=true
    username: yugabyte
    password: yugabyte
    driver-class-name: com.yugabyte.Driver
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      auto-commit: false
```

- **url** is the JDBC connection string. You can set YugabyteDB driver specific properties such as `load-balance` and `topology-keys` as part of this connection string.
- **driver-class-name** is the JDBC driver class name.

Update the JDBC url with the appropriate `hostname` and `port` number details `jdbc:yugabytedb://[hostname:port]/yugabyte` in the application.yaml file. Remember to remove the square brackets. It is just a place holder to indicate the fields that need user inputs.

## Normal build and run

Navigate to `yb-ms-data/springboot` folder in the project:

```sh
cd yb-ms-data/springboot
```

To build the app:

```sh
gradle build
```

To run & test the app:

```sh
gradle bootRun
```

To run the app with `ysql` profile:

```sh
gradle -Dspring.profiles.active=ysql bootRun
```

## Native build and run (GraalVM: 20.0.1-graalce)

Navigate to `yb-ms-data/springboot` folder in the project:

```sh
cd yb-ms-data/springboot
```

To build the app:

```sh
gradle nativeCompile
```

To run the app with `ysql` profile:

```sh
./build/native/nativeCompile/yb-boot-data -Dspring.profiles.active=ysql
```
