plugins {
    id("java-library")
}

repositories {
    mavenLocal()
}

dependencies {
    labyProcessor("processor")
    labyApi("api")
}

tasks.compileJava {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}