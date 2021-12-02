plugins {
	id("net.labymod.gradle.legacyminecraft")
	id("net.labymod.gradle.mixin")
}

val minecraftGameVersion: String = "1.8.9"
val minecraftVersionTag: String = "1.8"

dependencies {
    labyProcessor()
    labyApi("v1_8")
    api(project(":core"))
}

legacyMinecraft {
	version(minecraftGameVersion)

    mainClass("net.minecraft.launchwrapper.Launch")
    args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
    args("--labymod-dev-environment", "true")
    args("--addon-dev-environment", "true")
}

mixin {
    compatibilityLevel("JAVA_8")
    minVersion("0.7.11")

    packageName("org.example.addons.v1_8.mixins")

    version(minecraftGameVersion)
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

