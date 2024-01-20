pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "VRNCore"

include("api")
include("plugin")
include("nms-1_20_R1")
include("nms-1_20_R2")
include("nms-1_20_R3")
