package com.matthewprenger.cursegradle

/**
 * Various options for CurseGradle. These affect the entire plugin and not just a single curse project.
 */
class Options {

    /**
     * Debug mode will stop just short of actually uploading the file to Curse, and instead spit out the JSON
     * to the console. Useful for testing your buildscript.
     */
    boolean debug = false

    /**
     * If this is left enabled, CurseGradle will automatically detect the compatible versions of Java for the project
     * and add them to the CurseForge metadata.
     */
    boolean javaVersionAutoDetect = true

    /**
     * Enables Java version auto detection for Java 9 and beyond. Only applicable if {@link #javaVersionAutoDetect}
     * is enabled.
     */
    boolean detectNewerJava = false

    /**
     * Enable integration with the Gradle Java plugin. This includes setting the default artifact to the jar task.
     */
    boolean javaIntegration = true

    /**
     * Enable integration with the ForgeGradle plugin. This includes setting dependencies on the reobfuscation tasks.
     */
    boolean forgeGradleIntegration = true
}
