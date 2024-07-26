plugins {
    `java-library`
   	id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}


javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}


dependencies {
    // Ini4j
    implementation("org.ini4j:ini4j:0.5.4")
    
    // ZStd - Compression
    implementation("com.github.luben:zstd-jni:1.5.6-3")
    
    // Ikonli
    implementation("org.kordamp.desktoppanefx:desktoppanefx-core:0.15.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
    
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // TinyLog2
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")   
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}