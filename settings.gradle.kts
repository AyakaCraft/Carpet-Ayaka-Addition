import groovy.json.JsonSlurper

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Jitpack"
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://maven.firstdark.dev/releases") // For ModPublisher
        }
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.replaymod.preprocess" -> {
                    useModule("com.github.Fallen-Breath:preprocessor:${requested.version}")
                }
            }
        }
    }
}

(JsonSlurper().parseText(file("settings.json").readText()) as Map<String, Map<String, Int>>)["versions"]?.forEach { versionEntry ->
    val versionName = versionEntry.key
    val versionNumber = versionEntry.value
    include(":$versionName")
    project(":$versionName").apply {
        projectDir = file("versions/$versionName")
        buildFileName = if (versionNumber >= 260000) {
            "../../common.gradle.kts"
        } else {
            "../../common.remap.gradle.kts"
        }
    }
}
