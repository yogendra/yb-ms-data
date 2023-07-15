plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.0"
    id("io.micronaut.aot") version "4.0.0"
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
//    aot {
//        // Please review carefully the optimizations enabled below
//        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
//        optimizeServiceLoading.set(false)
//        convertYamlToJava.set(false)
//        precomputeOperations.set(true)
//        cacheEnvironment.set(true)
//        optimizeClassLoading.set(true)
//        deduceEnvironment.set(true)
//        optimizeNetty.set(true)
//    }
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("com.yugabyte:jdbc-yugabytedb:42.3.5-yb-1")
    implementation("org.postgresql:postgresql:42.5.1")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("org.fusesource.jansi:jansi:2.4.0")
}


application {
    mainClass.set("io.mservice.todo.TodoApplication")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

