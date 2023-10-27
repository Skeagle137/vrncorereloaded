plugins {
    id("io.papermc.paperweight.userdev") version "1.5.6"
}

dependencies {
    compileOnly(project(":api"))
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.20.2-R0.1-SNAPSHOT")
    compileOnly("net.skeagle:VRNLib:2.1.3")
}

tasks.build {
    dependsOn("reobfJar")
}