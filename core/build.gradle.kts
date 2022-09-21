version = "0.1.0"

plugins {
    id("java-library")
}

repositories {
    mavenLocal()
}

dependencies {
    labyProcessor()
    api(project(":api"))
}

addon {
    internalRelease()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}