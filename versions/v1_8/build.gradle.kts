plugins {
    id("net.labymod.gradle.legacyminecraft")
    id("net.labymod.gradle.volt")
}

dependencies {
    labyProcessor()
    labyApi("core")
    labyApi("v1_8")
    api(project(":core"))
}

legacyMinecraft {
    version("1.8.9")
    if (project.hasProperty("net.labymod.runconfig-java-version")) {
        javaVersion(project.property("net.labymod.runconfig-java-version") as String)
    }

    mainClass("net.minecraft.launchwrapper.Launch")
    args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
    args("--labymod-dev-environment", "true")
    args("--addon-dev-environment", "true")
}

intellij {
    minorMinecraftVersion("1.8")
    val javaVersion = project.findProperty("net.labymod.runconfig-v1_8-java-version")

    if(javaVersion != null) {
        run {
            javaVersion(javaVersion as String)
        }
    }
}
