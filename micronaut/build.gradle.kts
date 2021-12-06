plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.micronaut.application") version "3.0.0"
}

version = "1.0.0"
group = "io.mservice.micronaut"

repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.mservice.*")
    }
}

dependencies {
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
    implementation("com.yugabyte:jdbc-yugabytedb:42.3.0")
}


application {
    mainClass.set("io.mservice.todo.TodoApplication")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
    targetCompatibility = JavaVersion.toVersion("11")
}

