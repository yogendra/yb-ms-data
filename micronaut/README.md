[![Micronaut CI with Gradle](https://github.com/srinivasa-vasu/yb-ms-data/actions/workflows/gradle-micronaut.yml/badge.svg?branch=main)](https://github.com/srinivasa-vasu/yb-ms-data/actions/workflows/gradle-micronaut.yml)

[Micronaut](https://micronaut.io/) is a modern, jvm-based, full-stack framework for building modular, easily testable microservice and serverless applications.

This describes how to build a simple JPA based web application using Micronaut framework for YSQL API using [Yugabyte JDBC Driver](https://docs.yugabyte.com/latest/integrations/jdbc-driver/).

## Prerequisites

- Follow [YB Quick start](https://docs.yugabyte.com/latest/quick-start/) instructions to run a local YugabyteDB cluster. Test YugabyteDB's YSQL API, as [documented](../../quick-start/explore/ysql/) so that you can confirm that you have YSQL service running on `localhost:5433`.
- You will need JDK 11 or above. You can use [SDKMAN](https://sdkman.io/install) to install the JDK runtime.

## Get Started

You can find the complete source at [java framework with smart driver for YSQL](https://github.com/yugabyte/yb-ms-data.git). This project has directories for different java frameworks such as spring-boot, quarkus and micronaut. Clone this repository to a local workstation and open the `yb-ms-data` directory in your favorite IDE to easily navigate and explore Micronaut's project files.

```sh
git clone https://github.com/srinivasa-vasu/yb-ms-data.git
```

## Dependencies

This project depends on the following libraries.
```gradle
annotationProcessor("io.micronaut:micronaut-http-validation")
annotationProcessor("io.micronaut.data:micronaut-data-processor")
annotationProcessor("io.micronaut.openapi:micronaut-openapi")
implementation("io.micronaut:micronaut-http-client")
implementation("io.micronaut:micronaut-management")
implementation("io.micronaut:micronaut-runtime")
implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
implementation("io.micronaut.flyway:micronaut-flyway")
implementation("io.micronaut.sql:micronaut-jdbc-hikari")
implementation("io.swagger.core.v3:swagger-annotations")
implementation("javax.annotation:javax.annotation-api")
runtimeOnly("ch.qos.logback:logback-classic")
implementation("io.micronaut:micronaut-validation")
implementation("com.yugabyte:jdbc-yugabytedb:42.3.5-yb-1")
```
Update the driver dependency library **("com.yugabyte:jdbc-yugabytedb")** to the latest version. Grab the latest version from [Yugabyte JDBC driver](https://docs.yugabyte.com/latest/integrations/jdbc-driver/).

## Driver Configuration

Refer to the file `yb-ms-data/micronaut/src/main/resources/application.yaml` in the project directory:

```yml
datasources:
  default:
    url: jdbc:yugabytedb://[hostname:port]/yugabyte
    driverClassName: com.yugabyte.Driver
    data-source-properties:
      load-balance: true
      currentSchema: todo
    username: yugabyte
    password: yugabyte
    minimum-idle: 5
    maximum-pool-size: 20
```

- **url** is the JDBC connection string.
- **driverClassName** is the JDBC driver class name.
- **data-source-properties** is where YugabyteDB driver specific properties such as `load-balance` and `topology-keys` can be set.

Update the JDBC url with the appropriate `hostname` and `port` number details `jdbc:yugabytedb://[hostname:port]/yugabyte` in the application.yaml file. Remember to remove the square brackets. It is just a place holder to indicate the fields that need user inputs.

## Normal build and run

Navigate to `yb-ms-data/micronaut` folder in the project:

```sh
cd yb-ms-data/micronaut
```

To build the app:

```sh
gradle build
```

## Run the app

To run & test the app:

```sh
gradle run
```

To run the app with `ysql` profile:
```sh
gradle -Dmicronaut.environments=ysql run
```

## Native build and run (GraalVM: 20.0.1-graalce)

Navigate to `yb-ms-data/micronaut` folder in the project:

```sh
cd yb-ms-data/micronaut
```

To build the app:

```sh
gradle nativeCompile
```

To run the app with `ysql` profile:

```sh
./build/native/nativeCompile/yb-micronaut-data -Dmicronaut.environments=ysql
```
