plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "net.labymod.addons"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "net.labymod.addons.itemphysics" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {

            accessWidener(file("./game-runner/src/${this.sourceSetName}/resources/itemphysics-${versionId}.accesswidener"))

            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    devLogin = false
                }
            }
        }
    }

    addonInfo {
        namespace = "itemphysics"
        displayName = "ItemPhysics"
        author = "LabyMedia GmbH"
        minecraftVersion = "*"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version

    // The version modules compile against the Java version their Minecraft version runs on, so the
    // shared modules have to stay below the oldest of them instead of following the build JDK.
    extensions.findByType(JavaPluginExtension::class.java)?.apply {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}