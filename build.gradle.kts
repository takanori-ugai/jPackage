import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.panteleyev.jpackage.ImageType

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "2.2.0"
    id("org.panteleyev.jpackageplugin") version "1.7.3"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainModule.set("test.kotlin")
    mainClass.set("org.example.MainKt")
}

tasks {
    register("copyDependencies", Copy::class) {
        from(configurations.runtimeClasspath).into(layout.buildDirectory.dir("jmods"))
    }

    register("copyJar", Copy::class) {
        from(jar).into(layout.buildDirectory.dir("jmods"))
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    test {
        useJUnitPlatform()
    }

    jpackage {
        dependsOn("build", "copyDependencies", "copyJar")

        input = layout.buildDirectory.dir("jmods")
        destination = layout.buildDirectory.dir("dist")
        appName = "ApplicationName"
        vendor = "app.org"
        copyright = "Copyright (c) 2025 Takanori Ugai"
        mainJar = jar.get().archiveFileName.get()
        mainClass = "org.example.MainKt"
        javaOptions = listOf("-Dfile.encoding=UTF-8")

        mac {
            icon.set(file("icons/icons.icns"))
        }

        windows {
//        icon = layout.projectDirectory.file("icons/icons.ico")
            type = ImageType.MSI
            winConsole = false
            winDirChooser = true
            winMenu = true
        }

        linux {
            type = ImageType.DEFAULT
        }
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}
