plugins {
    id("org.spongepowered.gradle.vanilla")
    id("net.labymod.gradle.mixin")
}

version = "1.0.0"

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
}

minecraft {
    version("1.17.1")
    runs {
        client() {
            mainClass("net.minecraft.launchwrapper.Launch")
            args("--tweakClass", "net.labymod.core.vanilla.LabyModTweaker", "--dev")
        }
    }
}

dependencies {
    annotationProcessor("net.labymod.labymod4:addon-annotation-processor:0.1.0-${project.property("net.labymod.addon-release-type")}")
    api(project(":core"))
    api("net.labymod.labymod4:v1_17:0.1.0-${project.property("net.labymod.addon-release-type")}")
}

mixin {
    version("1.17.1")
    addReferenceMap(sourceSets.findByName("main"), "labymod.refmap.json")
}
