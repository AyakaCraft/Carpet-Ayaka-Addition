import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import nl.javadude.gradle.plugins.license.header.HeaderDefinitionBuilder
import java.util.*

plugins {
    id("net.fabricmc.fabric-loom-remap") version ("1.15.0-alpha.23")

    // https://github.com/ReplayMod/preprocessor
    // https://github.com/Fallen-Breath/preprocessor
    id("com.replaymod.preprocess") version ("c5abb4fb12")

    // https://github.com/GradleUp/shadow
    id("com.gradleup.shadow") version ("9.3.0")

    // https://github.com/hierynomus/license-gradle-plugin
    id("com.github.hierynomus.license") version ("0.16.1")

    // https://github.com/Fallen-Breath/yamlang
    id("me.fallenbreath.yamlang") version ("1.5.0")

    // https://github.com/firstdarkdev/modpublisher
    id("com.hypherionmc.modutils.modpublisher") version ("2.1.8")

    `maven-publish`
    idea
}

val properties = project.properties ?: mapOf<String, Any?>()

val mcVersionNumber = properties["mcVersion"] as Int
val minecraftVersion = properties["minecraft_version"].toString()

val jitpack = System.getenv("JITPACK") == "true"
val releasing = System.getenv("BUILD_RELEASE") == "true"
val ci = jitpack || releasing

repositories {
    mavenCentral()
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
        content {
            includeGroup("net.fabricmc")
        }
    }
    maven {
        name = "masa"
        url = uri("https://masa.dy.fi/maven")
        content {
            includeGroup("carpet")
        }
    }
    maven {
        name = "Fallen"
        url = uri("https://maven.fallenbreath.me/releases")  // Fallen orz
        content {
            includeGroup("me.fallenbreath")
            includeGroup("carpettisaddition")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
        content {
            includeGroup("com.terraformersmc")
        }
    }
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://maven.fallenbreath.me/jitpack")
    }
}

idea {
    module {
        if (!ci) {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}

// https://github.com/FabricMC/fabric-loader/issues/783
configurations {
    modRuntimeOnly { exclude(group = "net.fabricmc", module = "fabric-loader") }
}

dependencies {
    // loom
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    val parchment = properties["parchment"]
    if (parchment != null) {
        mappings(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$minecraftVersion:$parchment@zip")
        })
    } else {
        mappings(loom.officialMojangMappings())
    }

    // fabric
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")

    //libs
    include((modImplementation("me.fallenbreath:conditional-mixin-fabric:${properties["conditionalmixin_version"]}") as Dependency))

    // mods
    modImplementation("carpet:fabric-carpet:${properties["carpet_core_version"]}")

    modImplementation("carpettisaddition:carpet-tis-addition:${properties["tis_version"]}") {
        exclude(group = "carpet", module = "fabric-carpet")
    }

    if (mcVersionNumber >= 12000) {
        modImplementation("maven.modrinth:gca:${properties["gugle_version"]}") {
            exclude(group = "carpet", module = "fabric-carpet")
        }
    } else {
        modCompileOnly("maven.modrinth:gca:${properties["gugle_version"]}") {
            exclude(group = "carpet", module = "fabric-carpet")
        }
    }

    if (!ci) {
        // For runtime mods
        modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

        if (mcVersionNumber >= 11500) {
            modRuntimeOnly("com.terraformersmc:modmenu:${properties["modmenu_version"]}")
        } else {
            modRuntimeOnly("maven.modrinth:modmenu:${properties["modmenu_version"]}")
        }

        if (mcVersionNumber in 11600..<12100) {
            if (mcVersionNumber < 11900) {
                modRuntimeOnly("maven.modrinth:lazydfu:0.1.2")
            } else {
                modRuntimeOnly("maven.modrinth:lazydfu:0.1.3")
            }
        }

        // WHY DO YOU USE REFLECTION?????
        // modRuntimeOnly("curse.maven:xaeros-minimap-263420:${properties["xaeros_minimap_version"]}")
    }

    testImplementation("net.fabricmc:fabric-loader-junit:${properties["loader_version"]}")


    compileOnly("org.jetbrains:annotations:${properties["jetbrains_version"]}")
    annotationProcessor("org.jetbrains:annotations:${properties["jetbrains_version"]}")

}

val mixinConfigPath = "carpet-ayaka-addition.mixins.json"
val langDir = "assets/carpet-ayaka-addition/lang"
val javaCompatibility = if (mcVersionNumber >= 12005) {
    JavaVersion.VERSION_21
} else if (mcVersionNumber >= 11800) {
    JavaVersion.VERSION_17
} else if (mcVersionNumber >= 11700) {
    JavaVersion.VERSION_16
} else {
    JavaVersion.VERSION_1_8
}
val mixinCompatibility = javaCompatibility

loom {
    var commonVmArgs = listOf("-Dmixin.debug.export=true")
    runConfigs.configureEach {
        runDir = "../../run/${mcVersionNumber}"
        vmArgs(commonVmArgs)
        ideConfigGenerated(true)
    }

    runs {
        var auditVmArgs = commonVmArgs
        create("serverMixinAudit") {
            server()
            vmArgs(auditVmArgs)
            ideConfigGenerated(false)
        }
        create("clientMixinAudit") {
            client()
            vmArgs(auditVmArgs)
            ideConfigGenerated(false)
        }
    }

    mixin.useLegacyMixinAp = true
}

preprocess {
    patternAnnotation = "com.ayakacraft.carpetayakaaddition.utils.preprocess.PreprocessPattern"
}

// https://github.com/Fallen-Breath/yamlang
yamlang {
    targetSourceSets = listOf(sourceSets["main"])
    inputDir = langDir
}

tasks.shadowJar {
    configurations = listOf(project.configurations["shadow"])
    exclude("META-INF")
    archiveClassifier = "shadow"
}

tasks.withType<ShadowJar> {
    enableAutoRelocation = true
    relocationPrefix = "carpetayakaaddition.libs"
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    mustRunAfter(tasks.shadowJar)
    inputFile = tasks.shadowJar.get().archiveFile
}

val modVersion = properties["mod_version"].toString()

var modVersionSuffix = ""
var artifactVersion = modVersion
var artifactVersionSuffix = ""
// detect github action environment variables
// https://docs.github.com/en/actions/learn-github-actions/environment-variables#default-environment-variables
if (!releasing) {
    modVersionSuffix += "-SNAPSHOT"
    artifactVersionSuffix = "-SNAPSHOT"  // A non-release artifact is always a SNAPSHOT artifact
}
var fullModVersion = modVersion + modVersionSuffix
var fullProjectVersion: String
var fullArtifactVersion: String

// Example version values:
//   project.mod_version     1.0.3                      (the base mod version)
//   modVersionSuffix        +build.88                  (use github action build number if possible)
//   artifactVersionSuffix   -SNAPSHOT
//   fullModVersion          1.0.3+build.88             (the actual mod version to use in the mod)
//   fullProjectVersion      v1.0.3-mc1.15.2+build.88   (in build output jar name)
//   fullArtifactVersion     1.0.3-mc1.15.2-SNAPSHOT    (maven artifact version)

if (jitpack) {
    // move mc version into archivesBaseName, so jitpack will be able to organize archives from multiple subprojects correctly
    base.archivesName = "${properties["archives_base_name"]}-mc${minecraftVersion}"
    fullProjectVersion = "v${modVersion}${modVersionSuffix}"
    fullArtifactVersion = artifactVersion + artifactVersionSuffix
} else {
    base.archivesName = properties["archives_base_name"].toString()
    fullProjectVersion = "v${modVersion}-mc${minecraftVersion}${modVersionSuffix}"
    fullArtifactVersion = "${modVersion}-mc${minecraftVersion}${artifactVersionSuffix}"
}
version = fullProjectVersion

// See https://youtrack.jetbrains.com/issue/IDEA-296490
// if IDEA complains about "Cannot resolve resource filtering of MatchingCopyAction" and you want to know why
tasks.processResources {
    inputs.apply {
        property("id", properties["mod_id"].toString())
        property("name", properties["mod_name"].toString())
        property("version", fullModVersion)
    }

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "id" to properties["mod_id"],
                "name" to properties["mod_name"],
                "version" to fullModVersion,
                "carpet_dependency" to properties["carpet_dependency"],
                "minecraft_dependency" to properties["minecraft_dependency"],
                "loader_dependency" to properties["loader_dependency"]
            )
        )
    }

    filesMatching(mixinConfigPath) {
        expand(mapOf("COMPATIBILITY_LEVEL" to "JAVA_${mixinCompatibility.majorVersion}"))
    }
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
    if (javaCompatibility <= JavaVersion.VERSION_1_8) {
        // suppressed "source/target value 8 is obsolete and will be removed in a future release"
        options.compilerArgs.add("-Xlint:-options")
    }
}

java {
    sourceCompatibility = javaCompatibility
    targetCompatibility = javaCompatibility

    withSourcesJar()
}

tasks.jar {
    inputs.property("archives_base_name", properties["archives_base_name"])
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${properties["archives_base_name"]}" }
    }
}

tasks.shadowJar {
    inputs.property("archives_base_name", properties["archives_base_name"])
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${properties["archives_base_name"]}" }
    }
}

// https://github.com/hierynomus/license-gradle-plugin
license {
    // use "gradle licenseFormat" to apply license headers
    header = rootProject.file("HEADER")
    include("**/*.java")
    skipExistingHeaders = true

    headerDefinition(
        // ref: https://github.com/mathieucarbou/license-maven-plugin/blob/4c42374bb737378f5022a3a36849d5e23ac326ea/license-maven-plugin/src/main/java/com/mycila/maven/plugin/license/header/HeaderType.java#L48
        // modification: add a newline at the end
        HeaderDefinitionBuilder("SLASHSTAR_STYLE_NEWLINE")
            .withFirstLine("/*")
            .withBeforeEachLine(" * ")
            .withEndLine(" */\n")
            .withFirstLineDetectionDetectionPattern("(\\s|\\t)*/\\*.*$")
            .withLastLineDetectionDetectionPattern(".*\\*/(\\s|\\t)*$")
            .withNoBlankLines()
            .multiline()
            .noPadLines()
    )
    mapping("java", "SLASHSTAR_STYLE_NEWLINE")
    ext {
        set("name", properties["mod_name"].toString())
        set("author", "Calboot")
        set("year", Calendar.getInstance().get(Calendar.YEAR).toString())
    }
}
tasks["classes"].dependsOn(tasks.licenseFormatMain)
tasks["testClasses"].dependsOn(tasks.licenseFormatTest)

val minecraftVersions = properties["game_versions"].toString().split("\n")

// https://github.com/firstdarkdev/modpublisher
publisher {

    apiKeys {
        modrinth(System.getenv("MODRINTH_TOKEN") ?: "unset")
        curseforge(System.getenv("CURSEFORGE_TOKEN") ?: "unset")
        github(System.getenv("GITHUB_TOKEN") ?: "unset")
    }

    // debug = true

    if (properties["curseforge_id"] != null) {
        curseID = properties["curseforge_id"].toString()
    }
    if (properties["modrinth_id"] != null) {
        modrinthID = properties["modrinth_id"].toString()
    }

    versionType = properties["mod_version_type"].toString()
    changelog = rootProject.file("changelog.md")

    projectVersion = fullProjectVersion
    gameVersions = minecraftVersions
    loaders = listOf("fabric")
    curseEnvironment = "server"

    artifact.set(tasks.remapJar)

    if (mcVersionNumber < 11700) {
        setJavaVersions(JavaVersion.VERSION_1_8)
    }

    curseDepends {
        required("carpet")
        optional("carpet-tis-addition", "guglecarpetaddition")
    }

    modrinthDepends {
        required("carpet")
        optional("carpet-tis-addition", "gca")
    }

    github {
        repo(System.getenv("REPO"))
        tag(System.getenv("TAG"))
    }

}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = base.archivesName.get()
            version = fullArtifactVersion
        }
    }

    // select the repositories you want to publish to
    repositories {
        // maven {
        //     name = "AyakaCraft"
        //     url = uri("https://mc.ayakacraft.com:7000/releases")
        //     credentials<PasswordCredentials>(PasswordCredentials::class) {
        //         username = "Publisher"
        //         password = System.getenv("AYAKA_MAVEN_TOKEN")
        //     }
        //     authentication {
        //         create("basic", BasicAuthentication::class)
        //     }
        // }
    }
}
