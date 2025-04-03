package com.matthewprenger.cursegradle

import javax.annotation.Nullable

import static com.matthewprenger.cursegradle.Util.check

class CurseProject {

    /**
     * The gradle task that will upload this project
     */
    CurseUploadTask uploadTask

    /**
     * The main artifact that will be uploaded to CurseForge
     * <p>
     * Valid artifact types include:
     * <ol>
     *     <li>{@link File} objects</li>
     *     <li>A string path to a file</li>
     *     <li>Gradle {@link org.gradle.api.tasks.bundling.AbstractArchiveTask archive tasks}</li>
     * </ol>
     * </p>
     */
    CurseArtifact mainArtifact

    /**
     * The collection of additional artifacts that should be uploaded
     */
    final Collection<CurseArtifact> additionalArtifacts = new ArrayList<>()

    /**
     * The project ID on curseforge
     */
    def id

    /**
     * The default release type for this project's artifacts
     */
    @Nullable
    def releaseType

    /**
     * The type of changelog. At the time of writing this is: html and text
     */
    def changelogType = 'text'

    /**
     * The default changelog for this project's artifacts
     */
    def changelog = ''

    /**
     * The API key to be used for file uploads
     */
    def apiKey

    List<Object> gameVersionStrings = new ArrayList<>()

    @Nullable
    Set<Closure<?>> curseRelations

    /**
     * Set the main artifact to upload
     *
     * @param artifact The artifact
     * @param configClosure Optional configuration closure
     */
    void mainArtifact(def artifact, @DelegatesTo(CurseArtifact) Closure configClosure = null) {
        CurseArtifact curseArtifact = new CurseArtifact()
        if (configClosure != null) {
            curseArtifact.with(configClosure)
        }
        curseArtifact.artifact = artifact
        mainArtifact = curseArtifact
    }

    /**
     * Add an additional artifact to this project
     *
     * @param artifact The artifact
     * @param configClosure Optional configuration closure
     */
    void addArtifact(def artifact, @DelegatesTo(CurseArtifact) Closure configClosure = null) {
        CurseArtifact curseArtifact = new CurseArtifact()
        if (configClosure != null) {
            curseArtifact.with(configClosure)
        }
        curseArtifact.artifact = artifact
        additionalArtifacts.add(curseArtifact)
    }

    /**
     * Add a compatible game version
     *
     * @param version The version
     */
    void addGameVersion(def version) {
        gameVersionStrings.add(version)
    }

    /**
     * Configure CurseForge relations. Currently the only relation types are project relations
     *
     * @param configureClosure The configuration closure
     */
    void relations(@DelegatesTo(CurseRelation) Closure<?> configureClosure) {
        if (curseRelations == null) {
            curseRelations = new HashSet<>()
        }
        curseRelations.add(configureClosure)
    }

    /**
     * Copy the default project configurations to artifacts that are missing configurations
     */
    void copyConfig() {
        additionalArtifacts.each { artifact ->
            if (artifact.changelog == null && this.changelog != null) {
                artifact.changelog = this.changelog
            }
            if (artifact.changelogType == null && this.changelogType != null) {
                artifact.changelogType = this.changelogType
            }
            if (artifact.releaseType == null && this.releaseType != null) {
                artifact.releaseType = this.releaseType
            }
            if (curseRelations != null) {
                curseRelations.each {
                    artifact.relations(it)
                }
            }
        }

        if (mainArtifact != null) {
            if (mainArtifact.releaseType == null && this.releaseType != null) {
                mainArtifact.releaseType = this.releaseType
            }
            if (mainArtifact.changelogType == null && this.changelogType != null) {
                mainArtifact.changelogType = this.changelogType
            }
            if (mainArtifact.changelog == null && this.changelog != null) {
                mainArtifact.changelog = this.changelog
            }
            mainArtifact.gameVersionStrings = this.gameVersionStrings
            if (curseRelations != null) {
                curseRelations.each {
                    mainArtifact.relations(it)
                }
            }
        }
    }

    /**
     * Validate this project and all of it's artifacts
     */
    void validate() {
        check(id != null, "Project id not set")
        check(apiKey != null, "apiKey not set for project $id")
        check(mainArtifact != null, "mainArtifact not set for project $id")
        check(!gameVersionStrings.isEmpty(), "No Minecraft version configured for project $id")
        mainArtifact.validate(id)
        additionalArtifacts.each { artifact -> artifact.validate(id) }
    }
}
