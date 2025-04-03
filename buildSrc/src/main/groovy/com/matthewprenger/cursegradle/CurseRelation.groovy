package com.matthewprenger.cursegradle

import com.google.common.base.Strings

import static com.matthewprenger.cursegradle.Util.check

class CurseRelation implements Serializable {

    private void addRelation(String typeIn, String slugIn) {
        projects.add(new Project(type: typeIn, slug: slugIn))
    }

    @Deprecated
    void requiredLibrary(String slugIn) {
        addRelation("requiredDependency", slugIn)
    }

    void requiredDependency(String slugIn) {
        addRelation("requiredDependency", slugIn)
    }

    void embeddedLibrary(String slugIn) {
        addRelation("embeddedLibrary", slugIn)
    }

    @Deprecated
    void optionalLibrary(String slugIn) {
        addRelation("optionalDependency", slugIn)
    }

    void optionalDependency(String slugIn) {
        addRelation("optionalDependency", slugIn)
    }

    void tool(String slugIn) {
        addRelation("tool", slugIn)
    }

    void incompatible(String slugIn) {
        addRelation("incompatible", slugIn)
    }

    // Catches a missing method exception and gives a more user friendly error message
    def methodMissing(String name, def args) {
        check(false, "$name is not a valid relation type. Valid types are: $CurseGradlePlugin.VALID_RELATIONS")
    }

    Set<Project> projects = new HashSet<>()

    void validate(final def id) {
        projects.each { project -> project.validate(id) }
    }

    static class Project implements Serializable {

        /**
         * The unique slug for the project
         */
        String slug

        /**
         * The type of relationship. Must be one of {@link com.matthewprenger.cursegradle.CurseGradlePlugin#VALID_RELATIONS}
         */
        String type

        void validate(final def id) {
            check(!Strings.isNullOrEmpty(slug), "Project relation slug not set for relation in project $id")
            check(CurseGradlePlugin.VALID_RELATIONS.contains(type), "Invalid relation type ($type) for relation in project $id. Valid options are: $CurseGradlePlugin.VALID_RELATIONS")
        }

        @Override
        boolean equals(final o) {
            if (this.is(o)) {
                return true
            }
            if (getClass() != o.class) {
                return false
            }

            final Project project = (Project) o

            if (slug != project.slug) {
                return false
            }

            return true
        }

        @Override
        int hashCode() {
            return (slug != null ? slug.hashCode() : 0)
        }
    }
}
