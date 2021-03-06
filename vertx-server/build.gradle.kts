import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the Shadow plugin to add support for fatjar generation.
    id("com.github.johnrengelman.shadow").version("4.0.3")

    // Apply the application plugin to add support for building a standalone application.
    application
}

val VERTX_VERSION = "3.7.1"

dependencies {
    // Use calculator model
    compile(project(":model"))

    // Use Vertx core
    compile("io.vertx:vertx-core:$VERTX_VERSION")

    // Use Vertx web
    compile("io.vertx:vertx-web:$VERTX_VERSION")

    // Use Vertx config
    compile("io.vertx:vertx-config:$VERTX_VERSION")

    // Use Kotlin Vertx integration
    compile("io.vertx:vertx-lang-kotlin:$VERTX_VERSION")

    // Use Vertx unit test
    testCompile("io.vertx:vertx-unit:$VERTX_VERSION")
}

application {
    mainClassName = "io.vertx.core.Launcher"
}

val MAIN_VERTICLE_NAME = "com.example.calculator.api.http.CalculusHttpServerVerticle"

tasks {
    getByName<JavaExec>("run") {
        args = listOf("run", MAIN_VERTICLE_NAME)
    }

    withType<ShadowJar> {
        manifest {
            attributes["Main-Verticle"] = MAIN_VERTICLE_NAME
        }
    }
}
