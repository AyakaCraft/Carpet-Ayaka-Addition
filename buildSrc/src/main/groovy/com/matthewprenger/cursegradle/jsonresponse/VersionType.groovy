package com.matthewprenger.cursegradle.jsonresponse

/**
 * A dependency
 */
class VersionType {

    /**
     * The unique ID
     */
    int id

    /**
     * The user friendly name
     */
    String name

    /**
     * The unique slug
     */
    String slug

    @Override
    String toString() {
        return "VersionType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}'
    }
}
