plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

val app_name = "paper"
val app_classifier = "pre-alpha"
val app_version = "0.1.0"
val app_codename = "Monster-Blood"

val app_main_class = "io.azraein.paperfx.Main"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}

dependencies {
    implementation(project(":ink"))

    // TinyLog2
    implementation("org.tinylog:tinylog-impl:2.7.0")   

    // LuaJ
    implementation("org.luaj:luaj-jse:3.0.1")
}


tasks.register<Jar>("fatJar") {

    archiveBaseName.set("$app_name")
    archiveVersion.set("$app_version")
    archiveClassifier.set("$app_classifier-$app_codename")

    from (sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)

    from({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")
        }.map {
            zipTree(it)
        }
    })

    manifest {
        attributes["Main-Class"] = "$app_main_class"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named("build") {
    dependsOn("fatJar")
}