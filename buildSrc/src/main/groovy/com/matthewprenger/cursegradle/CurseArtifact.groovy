package com.matthewprenger.cursegradle

import com.google.gson.annotations.SerializedName
import org.gradle.api.Project

import javax.annotation.Nullable

import static com.matthewprenger.cursegradle.Util.check

class CurseArtifact implements Serializable {

    /**
     * The file that should be uploaded
     */
    transient def artifact

    /**
     * <b>Internal Use Only</b>
     */
    @Nullable
    transient Collection<Object> gameVersionStrings

    /**
     * The curseforge file ID of the artifact.
     */
    transient Integer fileID

    /**
     * The type of changelog. At the time of writing this is: html and text
     */
    @SerializedName("changelogType")
    def changelogType

    /**
     * The changelog for this artifact. The {@link Object#toString()} method will be called to get the value
     */
    @SerializedName("changelog")
    def changelog

    /**
     * The user-friendly display name for this artifact. The {@link Object#toString()} method will be called to get the value
     */
    @Nullable
    @SerializedName("displayName")
    def displayName

    /**
     * The release type of this artifact. See {@link CurseGradlePlugin#VALID_RELEASE_TYPES} for valid release types
     */
    @SerializedName("releaseType")
    def releaseType

    /**
     * Internal use only. Will be set when game versions are resolved into their numerical representation
     */
    @Nullable
    @SerializedName("gameVersions")
    int[] gameVersions

    /**
     * Internal use only
     */
    @Nullable
    @SerializedName("parentFileID")
    Integer parentFileID

    @SerializedName("relations")
    CurseRelation curseRelations

    void relations(@DelegatesTo(CurseRelation) Closure configClosure) {
        CurseRelation relation = new CurseRelation()
        relation.with(configClosure)
        curseRelations = relation
    }

    /**
     * Validate this artifact
     */
    void validate(final def id) {
        check(artifact != null, "Artifact not configured for project $id")
        check(changelogType != null, "The changelogType was null for project $id")
        check(changelog != null, "The changelog was not set for project $id")
        check(releaseType != null, "The releaseType was not set for project $id")
        releaseType = Util.resolveString(releaseType)
        check(CurseGradlePlugin.VALID_RELEASE_TYPES.contains(releaseType), "Invalid release type ($releaseType) for project $id. Valid options are: $CurseGradlePlugin.VALID_RELEASE_TYPES")
        curseRelations.each { it.validate(id) }
    }

    /**
     * Resolve metadata into their final values
     */
    void resolve(Project project) {
        changelogType = Util.resolveString(changelogType)
        changelog = Util.resolveString(changelog)
        releaseType = Util.resolveString(releaseType)
        artifact = Util.resolveFile(project, artifact)
        if (displayName != null) {
            displayName = Util.resolveString(displayName)
        }
    }

    @Override
    String toString() {
        return "CurseArtifact{" +
                "artifact=" + artifact +
                ", changelogType=" + changelogType +
                ", changelog=" + changelog +
                ", displayName=" + displayName +
                ", releaseType='" + releaseType + '\'' +
                ", gameVersionStrings=" + gameVersionStrings +
                '}'
    }
}
