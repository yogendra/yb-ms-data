plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("java")
    // id("org.springframework.experimental.aot") version "0.11.2"
}

group = "io.mservice.boot"
version = "1.0.0"
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/release") }
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.9")
    implementation("org.springframework.retry:spring-retry")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("org.postgresql:postgresql:42.3.5")
    implementation("com.yugabyte:jdbc-yugabytedb:42.3.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:7.0.0")
    testImplementation("com.yugabyte:testcontainers-yugabytedb:1.0.0-beta-4")
    testImplementation("org.testcontainers:junit-jupiter:1.15.3")
}


tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    imageName = "humourmind/${project.name}:${project.version}"
    pullPolicy = org.springframework.boot.buildpack.platform.build.PullPolicy.IF_NOT_PRESENT
}

