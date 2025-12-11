import java.io.OutputStreamWriter
import java.net.HttpURLConnection

plugins {
    id("net.fabricmc.fabric-loom-remap") version ("1.14-SNAPSHOT") apply (false)

    // https://github.com/ReplayMod/preprocessor
    // https://github.com/Fallen-Breath/preprocessor
    id("com.replaymod.preprocess") version ("d452ef7612")

    // https://github.com/GradleUp/shadow
    id("com.gradleup.shadow") version ("9.3.0") apply (false)

    // https://github.com/hierynomus/license-gradle-plugin
    id("com.github.hierynomus.license") version ("0.16.1") apply (false)

    // https://github.com/Fallen-Breath/yamlang
    id("me.fallenbreath.yamlang") version ("1.5.0") apply (false)

    // https://github.com/firstdarkdev/modpublisher
    id("com.hypherionmc.modutils.modpublisher") version ("2.1.8") apply (false)

    `maven-publish`
    idea
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("group").get()

preprocess {
    strictExtraMappings = false

    val mc11404 = createNode("1.14.4", 1_14_04, "")
    val mc11502 = createNode("1.15.2", 1_15_02, "")
    val mc11605 = createNode("1.16.5", 1_16_05, "")
    val mc11701 = createNode("1.17.1", 1_17_01, "")
    val mc11802 = createNode("1.18.2", 1_18_02, "")
    val mc11904 = createNode("1.19.4", 1_19_04, "")
    val mc12001 = createNode("1.20.1", 1_20_01, "")
    val mc12006 = createNode("1.20.6", 1_20_06, "")
    val mc12101 = createNode("1.21.1", 1_21_01, "")
    val mc12103 = createNode("1.21.3", 1_21_03, "")
    val mc12104 = createNode("1.21.4", 1_21_04, "")
    val mc12105 = createNode("1.21.5", 1_21_05, "")
    val mc12108 = createNode("1.21.8", 1_21_08, "")
    val mc12110 = createNode("1.21.10", 1_21_10, "")
    val mc12111 = createNode("1.21.11", 1_21_11, "")

    mc12006.link(mc12001, file("versions/mapping_12006_12001.txt"))
    mc12001.link(mc11904, file("versions/mapping_12001_11904.txt"))
    mc11904.link(mc11802, file("versions/mapping_11904_11802.txt"))
    mc11802.link(mc11701, file("versions/mapping_11802_11701.txt"))
    mc11701.link(mc11605, file("versions/mapping_11701_11605.txt"))
    mc11605.link(mc11502, file("versions/mapping_11605_11502.txt"))
    mc11502.link(mc11404, file("versions/mapping_11502_11404.txt"))

    mc12006.link(mc12101, file("versions/mapping_12006_12101.txt"))
    mc12101.link(mc12103, file("versions/mapping_12101_12103.txt"))
    mc12103.link(mc12104, file("versions/mapping_12103_12104.txt"))
    mc12104.link(mc12105, file("versions/mapping_12104_12105.txt"))
    mc12105.link(mc12108, file("versions/mapping_12105_12108.txt"))
    mc12108.link(mc12110, file("versions/mapping_12108_12110.txt"))
    mc12110.link(mc12111, file("versions/mapping_12110_12111.txt"))
}

fun libsDir(p: Project): Directory {
    return p.layout.buildDirectory.dir("libs").get()
}

tasks.register("buildAndGather") {
    subprojects {
        dependsOn(tasks["build"])
    }
    doLast {
        println("Gathering builds")

        delete(fileTree(libsDir(rootProject)) { include("*") })
        subprojects {
            print("Copying files for ${name}...    ")
            val currentLibsDir = libsDir(this)
            copy {
                from(currentLibsDir) {
                    include("*.jar")
                    exclude("*-dev.jar", "*-sources.jar", "*-shadow.jar")
                }
                into(libsDir(rootProject))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
            println("Succeeded")
        }
    }
}

tasks.register("publishAll") {
    mustRunAfter(rootProject.tasks.named("buildAndGather"))
    subprojects {
        dependsOn(
            this.tasks.named("publishMod")
            //, this.tasks.named("publish")
        )
    }
}

tasks.register("publishLocal") {
    subprojects {
        dependsOn(this.tasks.named("publishToMavenLocal"))
    }
}

tasks.register("sendDiscord") {
    mustRunAfter(tasks.named("buildAndGather"), tasks.named("publishAll"))
    doLast {
        val env = System.getenv()
        val webhook = env["DISCORD_WEBHOOK"]
        if (webhook != null) {
            val msgBuilder = StringBuilder(
                "# Announcement\nNew version of project [${env["REPO"]}](${env["REPO_URL"]}) has been released, check it on [Github Release](${env["REPO_URL"]}/releases/tag/${env["TAG"]})\n## What's new:\n${file("changelog.md").readText()}\n"
            )
            val modrinth = providers.gradleProperty("modrinth_id")
            val curseforge = providers.gradleProperty("curseforge_id")
            val bl1 = modrinth.isPresent
            val bl2 = curseforge.isPresent
            if (bl1 || bl2) {
                msgBuilder.append("Try out on ")
            }
            if (bl1) {
                msgBuilder.append("[Modrinth](https://www.modrinth.com/mod/${modrinth.get()})")
                if (bl2) {
                    msgBuilder.append(" and [CurseForge](https://cflookup.com/${curseforge.get()})")
                }
            } else if (bl2) {
                msgBuilder.append("[CurseForge](https://cflookup.com/${curseforge.get()})")
            }
            val msg = msgBuilder.toString().replace("\n", "\\n")
            println("Sending json content to ${webhook}...")
            val content = "{\"content\" : \"${msg}\"}"
            println(content)

            val connection = uri(webhook).toURL().openConnection() as HttpURLConnection
            connection.setRequestMethod("POST")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setDoInput(true)
            connection.setDoOutput(true)
            val writer = OutputStreamWriter(connection.getOutputStream())
            writer.write(content)
            writer.flush()
            writer.close()
            connection.connect()

            val code = connection.getResponseCode()
            if (code in 200..<300) {
                print("Succeeded")
            } else {
                print("Failed")
            }
            println(" with code $code and message ${connection.getResponseMessage()}")
        }
    }
}
