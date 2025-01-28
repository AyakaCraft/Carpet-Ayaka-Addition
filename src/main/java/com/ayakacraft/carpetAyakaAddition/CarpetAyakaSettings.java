package com.ayakacraft.carpetAyakaAddition;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.COMMAND;
import static carpet.api.settings.RuleCategory.EXPERIMENTAL;

public class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    @Rule(categories = {AYAKA, COMMAND})
    public static boolean commandTpt = false;

    @Rule(categories = {AYAKA, COMMAND})
    public static boolean commandGoHome = false;

    @Rule(categories = {AYAKA, COMMAND})
    public static boolean commandWaypoint = false;

    @Rule(categories = {AYAKA, COMMAND})
    public static boolean commandC = false;

    @Rule(categories = {AYAKA, COMMAND})
    public static boolean commandClearItem = false;

    @Rule(categories = {AYAKA, EXPERIMENTAL})
    public static boolean disableBatSpawning = false;

}
