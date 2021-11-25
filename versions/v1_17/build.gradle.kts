plugins {
    id("org.spongepowered.gradle.vanilla")
    id("net.labymod.gradle.mixin")
}
val minecraftGameVersion = "1.17.1"

version = "1.0.0"

minecraft {
    version(minecraftGameVersion)
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.CLIENT)
    runs {
        client {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
            args("--labymod-dev-environment", "true")
            args("--addon-dev-environment", "true")
            jvmArgs("-Dmixin.debug=true")
        }
    }
}

dependencies {
    labyProcessor()
    labyApi("v1_17")
    api(project(":core"))
}

mixin {
    compatibilityLevel("JAVA_16")
    minVersion("0.8.2")

    packageName("org.example.addon.v1_17.mixins")

    version(minecraftGameVersion)
}
