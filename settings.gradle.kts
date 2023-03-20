pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "VRNCore"

include("api")
include("plugin")
include("nms-1_19_R1")
include("nms-1_19_R2")
include("nms-1_19_R3")
