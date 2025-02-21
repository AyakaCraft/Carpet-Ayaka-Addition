package com.ayakacraft.carpetAyakaAddition;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.COMMAND;
import static carpet.api.settings.RuleCategory.EXPERIMENTAL;

public class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "")
    //#endif
    public static boolean commandTpt = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "")
    //#endif
    public static boolean commandGoHome = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "")
    //#endif
    public static boolean commandWaypoint = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "")
    //#endif
    public static boolean commandC = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "")
    //#endif
    public static boolean commandKillItem = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL})
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL}, desc = "")
    //#endif
    public static boolean disableBatSpawning = false;

}
