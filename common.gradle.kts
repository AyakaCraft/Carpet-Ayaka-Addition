import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import nl.javadude.gradle.plugins.license.header.HeaderDefinitionBuilder
import java.util.*

plugins {
    id("net.fabricmc.fabric-loom") version ("1.17-SNAPSHOT")

    // https://github.com/ReplayMod/preprocessor
    // https://github.com/Fallen-Breath/preprocessor
    id("com.replaymod.preprocess") version ("c5abb4fb12")

    // https://github.com/GradleUp/shadow
    id("com.gradleup.shadow") version ("9.5.1")

    // https://github.com/hierynomus/license-gradle-plugin
    id("com.github.hierynomus.license") version ("0.16.1")

    // https://github.com/Fallen-Breath/yamlang
    id("me.fallenbreath.yamlang") version ("1.5.0")

    // https://github.com/firstdarkdev/modpublisher
    id("com.hypherionmc.modutils.modpublisher") version ("2.2.1")

    `maven-publish`
    idea
}

val mcVersionNumber = property("mcVersion") as Int
val minecraftVersion = property("minecraft_version").toString()

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
        url = uri("https://maven.fallenbreath.me/snapshots")  // Fallen orz
        content {
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
        name = "Nucleoid"
        url = uri("https://maven.nucleoid.xyz/")
        content {
            includeGroup("eu.pb4")
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

dependencies {
    // loom
    minecraft("com.mojang:minecraft:${minecraftVersion}")

    // fabric
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    //libs
    include((implementation("me.fallenbreath:conditional-mixin-fabric:${property("conditionalmixin_version")}") as Dependency))

    // mods
    implementation("carpet:fabric-carpet:${property("carpet_core_version")}")

    implementation("carpettisaddition:carpet-tis-addition:${property("tis_version")}") {
        exclude(group = "carpet", module = "fabric-carpet")
    }

    if (!ci && mcVersionNumber < 260300) {
        // For runtime mods
        runtimeOnly("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

        runtimeOnly("maven.modrinth:modmenu:${property("modmenu_version")}")

        runtimeOnly("curse.maven:xaeros-minimap-263420:${property("xaeros_minimap_version")}")
    }

    testImplementation("net.fabricmc:fabric-loader-junit:${property("loader_version")}")


    compileOnly("org.jetbrains:annotations:${property("jetbrains_version")}")
    annotationProcessor("org.jetbrains:annotations:${property("jetbrains_version")}")

}

val mixinConfigPath = "carpet-ayaka-addition.mixins.json"
val langDir = "assets/carpet-ayaka-addition/lang"
val javaCompatibility = JavaVersion.VERSION_25
val mixinCompatibility = javaCompatibility

loom {
    accessWidenerPath = file("carpet-ayaka-addition.accesswidener")

    runConfigs.configureEach {
        runDirectory = file("../../run/${mcVersionNumber}")
        jvmArguments = listOf("-Dmixin.debug.export=true")
        generateRunConfig = true
    }
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

val modVersion = property("mod_version").toString()

val archivesBaseName = property("archives_base_name").toString()
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
    base.archivesName = "${archivesBaseName}-mc${minecraftVersion}"
    fullProjectVersion = "v${modVersion}${modVersionSuffix}"
    fullArtifactVersion = artifactVersion + artifactVersionSuffix
} else {
    base.archivesName = archivesBaseName
    fullProjectVersion = "v${modVersion}-mc${minecraftVersion}${modVersionSuffix}"
    fullArtifactVersion = "${modVersion}-mc${minecraftVersion}${artifactVersionSuffix}"
}
version = fullProjectVersion

val fabricModMeta = mapOf(
    "id" to property("mod_id").toString(),
    "name" to property("mod_name").toString(),
    "version" to fullModVersion,
    "carpet_dependency" to property("carpet_dependency").toString(),
    "minecraft_dependency" to property("minecraft_dependency").toString(),
    "loader_dependency" to property("loader_dependency").toString()
)

// See https://youtrack.jetbrains.com/issue/IDEA-296490
// if IDEA complains about "Cannot resolve resource filtering of MatchingCopyAction" and you want to know why
tasks.processResources {
    from("carpet-ayaka-addition.accesswidener")

    inputs.properties(fabricModMeta)

    filesMatching("fabric.mod.json") {
        expand(fabricModMeta)
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
    dependsOn(tasks.shadowJar)
    mustRunAfter(tasks.shadowJar)
    from(zipTree(tasks.shadowJar.get().archiveFile))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    inputs.property("archives_base_name", archivesBaseName)
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${archivesBaseName}" }
    }
}

tasks.shadowJar {
    inputs.property("archives_base_name", archivesBaseName)
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${archivesBaseName}" }
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
        set("name", property("mod_name"))
        set("author", "Calboot")
        set("year", Calendar.getInstance().get(Calendar.YEAR).toString())
    }
}
tasks["classes"].dependsOn(tasks.licenseFormatMain)
tasks["testClasses"].dependsOn(tasks.licenseFormatTest)

val minecraftVersions = property("game_versions").toString().split("\n")

// https://github.com/firstdarkdev/modpublisher
publisher {

    apiKeys {
        modrinth(System.getenv("MODRINTH_TOKEN") ?: "unset")
        curseforge(System.getenv("CURSEFORGE_TOKEN") ?: "unset")
        github(System.getenv("GITHUB_TOKEN") ?: "unset")
    }

    // debug = true

    if (hasProperty("curseforge_id")) {
        curseID = property("curseforge_id").toString()
    }
    if (hasProperty("modrinth_id")) {
        modrinthID = property("modrinth_id").toString()
    }

    versionType = property("mod_version_type").toString()
    changelog = rootProject.file("changelog.md")

    projectVersion = fullProjectVersion
    gameVersions = minecraftVersions
    loaders = listOf("fabric")
    curseEnvironment = "server"

    artifact.set(tasks.jar)

    curseDepends {
        required("carpet")
        optional("carpet-tis-addition")
    }

    modrinthDepends {
        required("carpet")
        optional("carpet-tis-addition")
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
        maven {
            name = "AyakaCraft"
            url = uri("https://mc.ayakacraft.com:7000/releases")
            credentials<PasswordCredentials>(PasswordCredentials::class) {
                username = "Publisher"
                password = System.getenv("AYAKA_MAVEN_TOKEN")
            }
            authentication {
                create("basic", BasicAuthentication::class)
            }
        }
    }
}
