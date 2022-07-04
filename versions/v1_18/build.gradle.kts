plugins {
    id("net.labymod.gradle.vanilla")
    id("net.labymod.gradle.volt")
}

version = "1.0.0"


minecraft {
    version("1.18.1")
    runs {
        client() {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
            args("--labymod-dev-environment", "true")
            args("--addon-dev-environment", "true")
        }
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    labyProcessor()
    labyApi("core")
    labyApi("v1_18")
    api(project(":core"))
}