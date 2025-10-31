plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    application
}

val ktor_version = "2.3.12"
val exposed_version = "0.50.1"
val logback_version = "1.4.14"

group = "com.ktor"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Явно JDK 21
    }
}

kotlin {
    jvmToolchain(21) // Kotlin тоже JVM 21
}

application {
    mainClass.set("com.ktor.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")

    // JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.12")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.postgresql:postgresql:42.7.3")

    // Security
    implementation("io.ktor:ktor-server-auth-jwt:2.3.12")
    implementation("org.mindrot:jbcrypt:0.4")

    // WebSockets
    implementation("io.ktor:ktor-server-websockets:2.3.12")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.24")

    // OpenAPI / Swagger
    implementation("io.ktor:ktor-server-openapi:2.3.12")
    implementation("io.ktor:ktor-server-swagger:2.3.12")

}
