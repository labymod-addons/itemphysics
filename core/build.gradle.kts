plugins {
    id("java-library")
}

repositories {
    mavenLocal()
}

dependencies {
    annotationProcessor("net.labymod.labymod4:addon-annotation-processor:0.1.0-${project.property("net.labymod.addon-release-type")}")
    api(project(":api"))
}

addon {
    internalRelease()
}