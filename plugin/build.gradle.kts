plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":api"))
    implementation(project(":nms-1_20_R1", "reobf"))
    implementation(project(":nms-1_20_R2", "reobf"))

    compileOnly("net.skeagle:VRNLib:2.1.3")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.LeonMangler:SuperVanish:6.2.17")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {

    shadowJar {
        archiveClassifier.set("")
    }

    register("updatePlugins") {
        dependsOn("jar")
        doLast {
            copy {
                from("build/libs/")
                into("C:/Users/Skeagle/Documents/VRN Stuff/Dev 1.20/plugins")
                rename { "${rootProject.name} ${version}.jar" }
            }
        }
    }

    withType<JavaCompile> {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    withType<ProcessResources> {
        val props = mapOf("version" to version)
        inputs.properties(props)
            filteringCharset ="UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "net.skeagle"
            artifactId = rootProject.name
            from(components["java"])
        }
    }
}