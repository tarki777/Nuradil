
val ktor_version = "2.3.12"
val kotlin_version = "1.9.24"
val exposed_version = "0.50.1"
val ktor_openapi_version = "0.7.4"
val logback_version = "1.4.14"

kotlin {
    jvmToolchain(21) // Говорим Gradle использовать JDK 21 для компиляции
}
plugins {
    // Жёстко указываем версию Kotlin в блоке plugins
    kotlin("jvm") version "1.9.24" 
    kotlin("plugin.serialization") version "1.9.24"
    application
}
group = "com.ktor"
version = "0.0.1"

application {
    mainClass.set("com.ktor.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    
    // JSON Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")

    // --- Database (Exposed + PostgreSQL) ---
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.postgresql:postgresql:42.7.3") 
    
    // --- Security ---
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("org.mindrot:jbcrypt:0.4") // BCrypt
    
    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:$logback_version")

    // Testing (если нужно)
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
}