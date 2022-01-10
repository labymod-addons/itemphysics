plugins {
    id("org.spongepowered.gradle.vanilla")
    id("net.labymod.gradle.volt")
}
val minecraftGameVersion: String = "1.17.1"
val minecraftVersionTag: String = "1.17"

version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
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
            jvmArgs("-Dmixin.debug=true")
        }
    }
}

dependencies {
    labyProcessor()
    labyApi("v1_17")
    api(project(":core"))
}

volt {
    mixin {
        compatibilityLevel = "JAVA_16"
        minVersion = "0.8.2"
    }

    packageName("org.example.addon.v1_17.mixins")
    version = minecraftGameVersion
}

intellij {
    minorMinecraftVersion(minecraftVersionTag)
    val javaVersion = project.findProperty("net.labymod.runconfig-v1_17-java-version")

    if(javaVersion != null) {
        run {
            javaVersion(javaVersion as String)
        }
    }
}
