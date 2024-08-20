plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // Ini4j
    implementation("org.ini4j:ini4j:0.5.4")
    
    // ZStd - Compression
    implementation("com.github.luben:zstd-jni:1.5.6-3")
        
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // TinyLog2
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")   
    
    // LuaJ
    implementation("org.luaj:luaj-jse:3.0.1")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}