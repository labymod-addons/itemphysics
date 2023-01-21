dependencies {

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

afterEvaluate {
    configurations.forEach {
        val configurationName = it.name

        if (configurationName.startsWith("v1_8")) {
            it.resolutionStrategy {
                //
                force("com.google.guava:guava:23.5-jre")
                force("com.mojang:authlib:1.5.21")
                force("org.apache.logging.log4j:log4j-api:2.0-beta9")
                force("org.apache.logging.log4j:log4j-core:2.0-beta9")
            }
        }
    }

    sourceSets.forEach {
        dependencies.add(it.annotationProcessorConfigurationName, "net.labymod.labymod4:processor:0.1.0-local-unknown")
    }
}