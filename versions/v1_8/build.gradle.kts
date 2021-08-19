plugins {
	id("net.labymod.gradle.legacyminecraft")
	id("net.labymod.gradle.mixin")
}

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

dependencies {
	annotationProcessor("net.labymod.labymod4:addon-annotation-processor:0.1.0-${project.property("net.labymod.addon-release-type")}")
    api(project(":core"))
	api("net.labymod.labymod4:v1_8:0.1.0-${project.property("net.labymod.addon-release-type")}")
}

legacyMinecraft {
	version("1.8.9")
	mappingFile(File(rootProject.projectDir, "mappings/1.8.9.srg"))
}

mixin {
	version("1.8.9")
	addReferenceMap(sourceSets.findByName("main"), "labymod.refmap.json")
}