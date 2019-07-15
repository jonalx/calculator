import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.41")
}

repositories {
    jcenter()
}

val ASSERTK_VERSION = "0.18"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        jcenter()
    }

    dependencies {
        // Use the Kotlin JDK 8 standard library.
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // Kotlin reflection
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // Use the Kotlin test library.
        testImplementation("org.jetbrains.kotlin:kotlin-test")

        // Use the Kotlin JUnit integration.
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

        // Use AssertJ style assertions
        testCompile("com.willowtreeapps.assertk:assertk-jvm:$ASSERTK_VERSION")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}
