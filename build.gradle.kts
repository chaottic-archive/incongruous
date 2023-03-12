import org.lwjgl.lwjgl
import org.lwjgl.Lwjgl.Module.*

plugins {
    id("java")
    id("org.lwjgl.plugin") version "0.0.30"
    id("io.freefair.lombok") version "8.0.0-rc2"
}

group = "com.chaottic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    lwjgl {
        snapshot.`3_3_2`
        implementation(core, glfw, opengl, stb)
    }

    implementation("it.unimi.dsi:fastutil:8.5.11")
    implementation("org.joml:joml:1.10.5")
    implementation("org.jetbrains:annotations:23.0.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}