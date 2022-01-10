buildscript {
    repositories {
        var bearerToken = System.getenv("LABYMOD_BEARER_TOKEN")

        if (bearerToken == null && project.hasProperty("net.labymod.distributor.bearer-token")) {
            bearerToken = project.property("net.labymod.distributor.bearer-token").toString()
        }

        maven("https://dist.labymod.net/api/v1/maven/release/") {
            name = "LabyMod Distributor"

            authentication {
                create<HttpHeaderAuthentication>("header")
            }

            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "Bearer $bearerToken"
            }
        }


        maven("https://repo.spongepowered.org/repository/maven-public") {
            name = "SpongePowered Repository"
        }
        mavenLocal()
    }

    dependencies {
        classpath("net.labymod.gradle", "addon", "0.1.33")
    }
}

group = "org.example"
version = "1.0.0"

plugins.apply("net.labymod.gradle.addon")


subprojects {
    plugins.apply("java-library")
    plugins.apply("net.labymod.gradle.addon")

    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        mavenLocal()
    }
}

addon {
    addonInfo {
        id("itemphysic")
        displayName("Item Physic")
        author("Pascal Helmerich")
        version(System.getenv().getOrDefault("VERSION", "0.0.0"))
    }

    dev{
        releaseChannel = "local"
        commitReference = "unknown"
    }

    internalRelease()
}