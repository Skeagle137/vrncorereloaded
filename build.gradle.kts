plugins {
    `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

allprojects {
    apply(plugin = "java")

    group = "net.skeagle"
    version = "5.3.3"

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://jitpack.io")
        maven("https://repo.skeagle.net/snapshots")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://libraries.minecraft.net/")
        gradlePluginPortal()
        mavenLocal()
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
        compileOnly("com.mojang:authlib:3.16.29")
    }

    tasks.withType<JavaCompile> {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}
