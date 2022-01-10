plugins {
	id("net.labymod.gradle.legacyminecraft")
	id("net.labymod.gradle.volt")
}

val minecraftGameVersion: String = "1.8.9"
val minecraftVersionTag: String = "1.8"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

configurations.all {
    resolutionStrategy {
        force("com.google.guava:guava:17.0")
        // force("com.google.code.gson:gson:2.2.4")
        // For debugging purposes only.
        if (System.getenv("RUNNING_IN_PIPELINE") == null) {
            force("org.spongepowered:mixin:0.7.11-SNAPSHOT")
        }
        force("com.mojang:authlib:1.5.21")
        force("org.apache.logging.log4j:log4j-api:2.0-beta9")
        force("org.apache.logging.log4j:log4j-core:2.0-beta9")
    }

}

dependencies {
    labyProcessor()
    labyApi("v1_8")
    api(project(":core"))
    api("net.labymod.labymod4:processor:0.1.0-local-unknown");
}

legacyMinecraft {
	version(minecraftGameVersion)

    mainClass("net.minecraft.launchwrapper.Launch")
    jvmArgs("-Dmixin.debug=true")
    args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
    args("--labymod-dev-environment", "true")
    args("--addon-dev-environment", "true")
}

volt {
    mixin {
        compatibilityLevel = "JAVA_8"
        minVersion = "0.6.6"
    }

    packageName("net.labymod.addons.itemphysic.v1_8.mixins")
    version = minecraftGameVersion
}

intellij {
    minorMinecraftVersion(minecraftVersionTag)
    val javaVersion = project.findProperty("net.labymod.runconfig-v1_8-java-version")

    if(javaVersion != null) {
        run {
            javaVersion(javaVersion as String)
        }
    }
}

