plugins {
    id("net.labymod.gradle.vanilla")
    id("net.labymod.gradle.volt")
}

version = "1.0.0"


minecraft {
    version("1.17.1")
    runs {
        client() {
            requiresAssetsAndNatives.set(true)
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
    labyApi("v1_17")
    api(project(":core"))
}

volt {
    mixin {
        compatibilityLevel = "JAVA_16"
        minVersion = "0.8.2"
    }

    packageName("net.labymod.addons.itemphysic.v1_17.mixins")

    version = "1.17.1"
}
