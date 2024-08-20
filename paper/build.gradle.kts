plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}


javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}


dependencies {

    implementation(project(":ink"))

    // TinyLog2
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")   
    
    // LuaJ
    implementation("org.luaj:luaj-jse:3.0.1")
    
    // ControlsFX
    implementation("org.controlsfx:controlsfx:11.2.1")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}