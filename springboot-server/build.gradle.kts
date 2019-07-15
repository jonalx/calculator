import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply springboot plugin for adding springboot depedencies to the project
    id("org.springframework.boot").version("2.1.6.RELEASE")

    // Apply spring dependency manager plugin
    id("io.spring.dependency-management").version("1.0.8.RELEASE")

    // Apply spring plugin for spring-kotlin integration
    kotlin("plugin.spring").version("1.3.31")

    // Apply the application plugin to add support for building a standalone application.
    application
}

dependencies {
    // Use calculator model
    compile(project(":model"))

    // Use SpringBoot web module
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Support for Kotlin classes serializacion/deserialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Use SpringBoot test module
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

application {
    // Define the main class for the application
    mainClassName = "com.example.calculator.api.http.AppKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
