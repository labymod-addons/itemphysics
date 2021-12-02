plugins {
    id("org.spongepowered.gradle.vanilla")
    id("net.labymod.gradle.mixin")
}
val minecraftGameVersion = "1.18"
val minecraftVersionTag: String = "1.18"

version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

minecraft {
    version(minecraftGameVersion)
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.CLIENT)
    runs {
        client {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
            args("--labymod-dev-environment", "true")
            args("--addon-dev-environment", "true")
        }
    }
}

dependencies {
    labyProcessor()
    labyApi("v1_18")
    api(project(":core"))
}

mixin {
    compatibilityLevel("JAVA_17")
    minVersion("0.8.2")

    packageName("org.example.addons.v1_18.mixins")

    version(minecraftGameVersion)
}

intellij {
    minorMinecraftVersion(minecraftVersionTag)
    val javaVersion = project.findProperty("net.labymod.runconfig-v1_18-java-version")

    if(javaVersion != null) {
        run {
            javaVersion(javaVersion as String)
        }
    }
}
