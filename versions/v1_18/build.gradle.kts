version = "0.1.0"

plugins {
    id("net.labymod.gradle.vanilla")
    id("net.labymod.gradle.volt")
}

val minecraftGameVersion = "1.18.2"
val minecraftVersionTag: String = "1.18"

version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

minecraft {
    version(minecraftGameVersion)
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.CLIENT)
    runs {
        client {
            requiresAssetsAndNatives.set(true)
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
            args("--labymod-dev-environment", "true")
            args("--addon-dev-environment", "true")
            jvmArgs("-Dnet.labymod.running-version=$minecraftGameVersion")
        }
    }
}

volt {
    mixin {
        compatibilityLevel = "JAVA_17"
        minVersion = "0.8.2"
    }

    packageName("net.labymod.addons.itemphysics.v1_17.mixins")
    packageName("net.labymod.addons.itemphysics.v1_18.mixins")

    inheritFrom("v1_17")

    version = minecraftGameVersion
}

val inheritv117 = sourceSets.create("inherit-v1_17") {
    java.srcDirs(project.files("../v1_17/src/main/java"))
    java {
        exclude("net/labymod/v1_17/client/render/matrix/VersionedStackProvider.java")
    }
}

sourceSets.getByName("main") {
    java.srcDirs(inheritv117.java)
}

dependencies {
    annotationProcessor("net.labymod:sponge-mixin:0.1.0+0.11.2+mixin.0.8.5")
    labyProcessor()
    labyApi("v1_18")
    api(project(":core"))
}

intellij {
    minorMinecraftVersion(minecraftVersionTag)
    val javaVersion = project.findProperty("net.labymod.runconfig-v1_18-java-version")

    if (javaVersion != null) {
        run {
            javaVersion(javaVersion as String)
        }
    }
}

tasks {
    collectNatives {
        into("${project.gradle.gradleUserHomeDir}/caches/VanillaGradle/v2/natives/${minecraftGameVersion}/")
    }

    renameApiMixin {
        relocate("net.labymod.addons.itemphysics.v1_17.", "net.labymod.addons.itemphysics.v1_18.")
    }
}
