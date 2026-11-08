# jPackage

A small Kotlin/JVM desktop example that uses Gradle and the JDK's `jpackage` tool to build native application packages.

The sample prints `Hello World!` and opens a Swing window titled **Hello Swing** containing **Hello, Swing World!**. Native packages bundle a Java runtime, so users do not need to install Java separately.

## Requirements

- A full JDK with `jpackage` and `jlink`. The build configures a **Java 17 toolchain** and targets Java 17 bytecode; Gradle's Foojay resolver can provision that toolchain when needed.
- Internet access for the initial Gradle, plugin, dependency, and toolchain downloads.
- A graphical desktop session to run the Swing application.
- The native packaging tools required by your operating system and package format.

Use the included Gradle wrapper; a separate Gradle installation is not required. Set `JAVA_HOME` to a compatible JDK if necessary.

| Platform | Current packaging configuration | Additional setup |
| --- | --- | --- |
| Linux | Platform-default package format | Install the tools required by the selected format, such as `fakeroot` for Ubuntu DEB packages or `rpm-build` for RPM packages. |
| Windows | MSI installer, with a directory chooser and Start menu integration | Install a WiX Toolset version supported by your JDK's `jpackage`. |
| macOS | Platform-default package format with a custom icon | Provide `icons/icons.icns`, or remove/change the `mac { icon.set(...) }` configuration before packaging. The icon file is not included in this repository. |

Packages must be built on the target operating system; `jpackage` does not cross-compile installers. See the [JDK 17 packaging prerequisites](https://docs.oracle.com/en/java/javase/17/jpackage/packaging-overview.html) for platform details.

## Get started

```sh
git clone https://github.com/takanori-ugai/jPackage.git
cd jPackage
./gradlew build
./gradlew run
```

Close the Swing window to exit the application. On Windows, use `gradlew.bat` in place of `./gradlew`:

```powershell
.\gradlew.bat build
.\gradlew.bat run
```

## Create a native package

Run the packaging task on the operating system you are targeting:

```sh
./gradlew clean jpackage
```

On Windows:

```powershell
.\gradlew.bat clean jpackage
```

The `jpackage` task depends on `build`, `copyDependencies`, and `copyJar`. It stages the application JAR and its runtime dependencies in `build/jmods/`, then writes the native package to `build/dist/`. Despite the staging directory's name, the inputs are JAR files rather than JMOD archives.

| Location | Contents |
| --- | --- |
| `build/libs/` | Built application JARs |
| `build/jmods/` | Application JAR and runtime dependencies used by `jpackage` |
| `build/dist/` | Native packaging output |

The packaged application is currently named **ApplicationName**, with vendor **app.org** and project version **1.0**. The project name is currently `untitled`.

## Useful tasks

```sh
./gradlew build          # Compile, check, and assemble the project
./gradlew run            # Launch the Swing sample
./gradlew test           # Run tests when present
./gradlew ktlintCheck    # Check Kotlin formatting
./gradlew ktlintFormat   # Apply Kotlin formatting
./gradlew shadowJar      # Build a separate fat JAR
./gradlew jpackage       # Create a native package
```

The native packaging task uses the regular `jar` output and staged dependencies, rather than the `shadowJar` artifact.

## Customize the application

Edit [build.gradle.kts](build.gradle.kts) to change:

- `group` and `version` for project coordinates.
- `application.mainClass` and the packaging task's `mainClass` for the entry point.
- `application.mainModule` for the Java module name.
- `appName`, `vendor`, `copyright`, icons, and operating-system-specific installer options.
- `javaOptions` for JVM arguments; UTF-8 file encoding is currently configured.

If you rename the module or packages, also update [src/main/java/module-info.java](src/main/java/module-info.java). The current module, `test.kotlin`, requires `kotlin.stdlib` and `java.desktop` and exports `org.example`.

The application itself is in [src/main/kotlin/org/example/Main.kt](src/main/kotlin/org/example/Main.kt).

## Continuous integration

[Java CI](.github/workflows/gradle.yml) is configured to run `./gradlew build jpackage` on Ubuntu with JDK 17, 21, and 23. macOS and Windows packages must be built separately on those operating systems.

## References

- [JDK `jpackage` documentation](https://docs.oracle.com/en/java/javase/17/jpackage/packaging-overview.html)
- [JPackage Gradle Plugin](https://github.com/petr-panteleyev/jpackage-gradle-plugin)

## License

Licensed under the [Apache License, Version 2.0](LICENSE).
