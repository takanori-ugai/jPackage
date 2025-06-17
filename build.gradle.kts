import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.panteleyev.jpackage.ImageType

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "2.1.21"
    id("org.panteleyev.jpackageplugin") version "1.7.3"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainModule.set("test.kotlin")
    mainClass.set("org.example.MainKt")
}

tasks.register("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into(layout.buildDirectory.dir("jmods"))
}

tasks.register("copyJar", Copy::class) {
    from(tasks.jar).into(layout.buildDirectory.dir("jmods"))
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    input = layout.buildDirectory.dir("jmods")
    destination = layout.buildDirectory.dir("dist")
    appName = "ApplicationName"
    vendor = "app.org"
    copyright = "Copyright (c) 2025 Takanori Ugai"
    mainJar = tasks.jar.get().archiveFileName.get()
    mainClass = "org.example.MainKt"
    javaOptions = listOf("-Dfile.encoding=UTF-8")

    mac {
        icon.set(file("icons/icons.icns"))
    }

    windows {
//        icon = layout.projectDirectory.file("icons/icons.ico")
        type = ImageType.MSI
        winConsole = true
    }

    linux {
        type = ImageType.DEFAULT
    }
}

