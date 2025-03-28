package com.ayakacraft.carpetayakaaddition;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public final class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    public static final int ITEM_DISCARD_AGE_MAX_VALUE = 72000;

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
    @Rule(categories = {AYAKA, COMMAND},
            validators = UnsignedIntegerValidator.class,
            options = {"0", "5", "10", "30"},
            strict = false
    )
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Await seconds for /killitem", validate = UnsignedIntegerValidator.class, options = {"0", "5", "10", "30"}, strict = false)
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

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL},
            validators = ItemDiscardAgeValidator.class,
            options = {"0", "3000", "3600", "6000", "12000", "72000"},
            strict = false
    )
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL}, desc = "Item discard ticks", validate = ItemDiscardAgeValidator.class, options = {"0", "3000", "3600", "6000", "12000", "72000"}, strict = false)
    //#endif
    public static int itemDiscardAge = 6000;

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

    private static final class ItemDiscardAgeValidator extends Validator<Integer> {

        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return (newValue < 0 || newValue > ITEM_DISCARD_AGE_MAX_VALUE) ? null : newValue;
        }

        @Override
        public String description() {
            return "Must not be negative or larger than " + ITEM_DISCARD_AGE_MAX_VALUE;
        }

    }

}
