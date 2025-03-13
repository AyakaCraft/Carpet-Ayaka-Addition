package com.ayakacraft.carpetayakaaddition;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Teleport to players")
    //#endif
    public static boolean commandTpt = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Teleport to spawn point")
    //#endif
    public static boolean commandGoHome = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Global waypoints on server")
    //#endif
    public static boolean commandWaypoint = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Spectator Switching")
    //#endif
    public static boolean commandC = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Clear items")
    //#endif
    public static boolean commandKillItem = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND}, validators = UnsignedIntegerValidator.class, options = {"0", "5", "10", "30"})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Await seconds for /killitem", validate = UnsignedIntegerValidator.class, options = {"0", "5", "10", "30"})
    //#endif
    public static int killItemAwaitSeconds = 5;

    //#if MC>=11900
    @Rule(categories = {AYAKA, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, FEATURE}, desc = "Disable bat spawning")
    //#endif
    public static boolean disableBatSpawning = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, FEATURE}, desc = "Disable foxes picking up items")
    //#endif
    public static boolean foxNoPickupItem = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND, SURVIVAL})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND, SURVIVAL}, desc = "Better opPlayerNoCheat option")
    //#endif
    public static boolean betterOpPlayerNoCheat = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL, BUGFIX})
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL, BUGFIX}, desc = "Fake player resident ineffective in backups fix")
    //#endif
    public static boolean fakePlayerResidentBackupFix = false;

    private static final class UnsignedIntegerValidator extends Validator<Integer> {

        @Override
        public Integer validate(@Nullable final ServerCommandSource source, final CarpetRule<Integer> changingRule, final Integer newValue, final String userInput) {
            return newValue < 0 ? null : newValue;
        }

        @Override
        public String description() {
            return "Must not be negative";
        }

    }

}
