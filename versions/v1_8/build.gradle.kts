plugins {
	id("net.labymod.gradle.legacyminecraft")
	id("net.labymod.gradle.mixin")
}

val minecraftGameVersion = "1.8.9"

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

tasks.compileJava {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

