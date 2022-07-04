plugins {
    id("net.labymod.gradle.vanilla")
    id("net.labymod.gradle.volt")
}

version = "1.0.0"


minecraft {
    version("1.19")
    runs {
        client() {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
            args("--labymod-dev-environment", "true")
            args("--addon-dev-environment", "true")

            if (org.gradle.internal.os.OperatingSystem.current() == org.gradle.internal.os.OperatingSystem.MAC_OS) {
                jvmArgs("-XstartOnFirstThread")
            }
        }
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    labyProcessor()
    labyApi("core")
    labyApi("v1_19")
    api(project(":core"))
}