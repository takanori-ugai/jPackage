import org.panteleyev.jpackage.ImageType

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "2.1.0"
    id("org.panteleyev.jpackageplugin") version "1.6.0"
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

task("copyDependencies", Copy::class) {
    from(configurations.runtimeClasspath).into("$buildDir/jmods")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

}

task("copyJar", Copy::class) {
    from(tasks.jar).into("$buildDir/jmods")
}

tasks.jpackage {
    dependsOn("build", "copyDependencies", "copyJar")

    appName = "ApplicationName"
    vendor = "app.org"
    copyright = "Copyright (c) 2024 Takanori Ugai"
    runtimeImage = System.getProperty("java.home")
    module = "test.kotlin/org.example.MainKt"
    modulePaths = listOf("$buildDir/jmods")
    destination = "$buildDir/dist"
    javaOptions = listOf("-Dfile.encoding=UTF-8")

    mac {
        icon = "icons/icons.icns"
    }

    windows {
        type = ImageType.MSI
        icon = ""
        winConsole = true
    }

    linux {
        type = ImageType.DEFAULT
    }
}

tasks.register("removeReadOnlyFiles") {
    doLast {
        val directoryPath = "build"
        val directory = file(directoryPath)
        if (directory.exists()) {
            directory.walkTopDown().forEach { file ->
                if (file.isFile) {
                    file.setWritable(true)
                    file.delete()
                }
            }
        }
    }
}

tasks.clean {
    dependsOn("removeReadOnlyFiles")
}
