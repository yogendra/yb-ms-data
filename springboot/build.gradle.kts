
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
    id("org.graalvm.buildtools.native") version "0.9.20"
}

group = "io.mservice.boot"
version = "1.0.0"
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

extra["testcontainersVersion"] = "1.17.6"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
//    maven { url = uri("https://repo.spring.io/release") }
//    maven { url = uri("https://repo.spring.io/milestone") }
    mavenLocal()
}

dependencies {
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
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("humourmind/${project.name}:${project.version}")
    pullPolicy.set(org.springframework.boot.buildpack.platform.build.PullPolicy.IF_NOT_PRESENT)
}
