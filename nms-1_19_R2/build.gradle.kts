plugins {
    id("io.papermc.paperweight.userdev") version "1.5.1"
}

dependencies {
    compileOnly(project(":api"))
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.3-R0.1-SNAPSHOT")
    compileOnly("net.skeagle:VRNLib:2.1.3")
}

tasks.build {
    dependsOn("reobfJar")
}