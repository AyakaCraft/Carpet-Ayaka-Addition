/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public final class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    public static final String REINTRODUCE = "reintroduce";

    public static final String CHEAT = "cheat";

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND, CHEAT})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND, CHEAT}, desc = "Enables /tpt to teleport to another player in your server")
    //#endif
    public static boolean commandTpt = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND, CHEAT})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND, CHEAT}, desc = "Enables /gohome to teleport right back to your spawn point")
    //#endif
    public static boolean commandGoHome = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND, CHEAT})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND, CHEAT}, desc = "Enables /waypoint to manipulate and teleport to shared waypoints")
    //#endif
    public static boolean commandWaypoint = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND, CHEAT})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND, CHEAT}, desc = "Enables /c to switch your gamemode between spectator and survival")
    //#endif
    public static boolean commandC = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND})
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Enables /killitem to clear dropped items with one shot")
    //#endif
    public static boolean commandKillItem = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, COMMAND},
            validators = UnsignedIntegerValidator.class,
            options = {"0", "5", "10", "30"},
            strict = false
    )
    //#else
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Seconds to wait before clearing the items", validate = UnsignedIntegerValidator.class, options = {"0", "5", "10", "30"}, strict = false)
    //#endif
    public static int killItemAwaitSeconds = 5;

    //#if MC>=11900
    @Rule(categories = {AYAKA, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, FEATURE}, desc = "Disables natural spawning of bats")
    //#endif
    public static boolean disableBatSpawning = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, FEATURE}, desc = "Prevents foxes from picking up dropped items")
    //#endif
    public static boolean foxNoPickupItem = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, SURVIVAL, CHEAT})
    //#else
    //$$ @Rule(category = {AYAKA, SURVIVAL, CHEAT}, desc = "Stops operators from using /kill, /clear, /effect, /item and /difficulty", extra = {"Only active when Carpet Tis Addition is loaded and opPlayerNoCheat is set to true"})
    //#endif
    public static boolean betterOpPlayerNoCheat = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL, BUGFIX})
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL, BUGFIX}, desc = "Fixes the bug that fake players are not reconnected after retracements", extra = {"Only active when Gca is loaded and fakePlayerResident is set to true"})
    //#endif
    public static boolean fakePlayerResidentBackupFix = false;

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL},
            validators = ItemDiscardAgeValidator.class,
            options = {"0", "3000", "3600", "6000", "12000", "72000"},
            strict = false
    )
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL}, desc = "Modifies the ticks before an item entity is discarded", validate = ItemDiscardAgeValidator.class, options = {"0", "3000", "3600", "6000", "12000", "72000"}, extra = {"Set to 0 (or 6000) to use vanilla value", "Max value 72000 (an hour)"}, strict = false)
    //#endif
    public static int itemDiscardAge = 0;

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE}, desc = "Reintroduces the feature of cactuses, bamboos and sugarcanes being (wrongly) random-ticked on scheduled ticks in Minecraft 1.15.2 and lower")
    //#endif
    public static boolean forceTickPlantsReintroduce = false;

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

        public static final int ITEM_DISCARD_AGE_MAX_VALUE = 72000;

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
