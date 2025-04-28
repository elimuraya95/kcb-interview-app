plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.4.0"

}

group = "tech.elimuraya"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")

    // Lombok dependencies
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "eclipse-temurin:17-jre" // Base image for JDK 17 apps
    }
    to {
        image = "elimbaru/kcb-interview-app"
        tags = setOf("latest")
    }
    container {
        ports = listOf("8080") // Expose port
        environment = mapOf(
            "SERVER_PORT" to "8080"
        )
        jvmFlags = listOf(
            "-Xms512m",
            "-Xmx1024m"
        )
    }
}

