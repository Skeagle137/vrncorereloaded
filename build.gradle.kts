plugins {
    `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

allprojects {
    apply(plugin = "java")

    group = "net.skeagle"
    version = "5.3.1"

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://jitpack.io")
        maven("https://repo.skeagle.net/snapshots")
        maven("https://repo.codemc.io/repository/nms/")
        gradlePluginPortal()
        mavenLocal()
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
        compileOnly("com.mojang:authlib:3.5.41")
    }

    tasks.withType<JavaCompile> {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}
