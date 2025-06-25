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

import carpet.settings.Rule;
import com.ayakacraft.carpetayakaaddition.settings.conditions.Condition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.ForceTickPlantsReintroduceCondition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.LegacyHoneyBlockSlidingCondition;
import com.ayakacraft.carpetayakaaddition.settings.validators.ItemDiscardAgeValidator;
import com.ayakacraft.carpetayakaaddition.settings.validators.UnsignedIntegerValidator;

import static carpet.settings.RuleCategory.*;

public final class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    public static final String REINTRODUCE = "reintroduce";

    public static final String CHEAT = "cheat";

    public static final String BOT = "BOT";

    @Rule(
            category = {AYAKA, SURVIVAL, CHEAT},
            desc = "More commands for opPlayerNoCheat",
            extra = {
                    "Influenced commands: /kill, /clear, /effect, /item and /difficulty",
                    "Only active when Carpet Tis Addition is loaded and opPlayerNoCheat is set to true",
                    "You don't want to lose your pillagers, do you?"
            }
    )
    public static boolean betterOpPlayerNoCheat = false;

    @Rule(
            category = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"},
            desc = "Enables /address and /ad to manipulate and teleport to shared waypoints"
    )
    public static String commandAddress = "false";

    @Rule(
            category = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"},
            desc = "Enables /c to switch your gamemode between spectator and survival"
    )
    public static String commandC = "false";

    @Rule(
            category = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"},
            desc = "Enables /gohome to teleport right back to your spawn point"
    )
    public static String commandGoHome = "false";

    @Rule(
            category = {AYAKA, COMMAND},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"},
            desc = "Enables /killitem to clear dropped items with one shot"
    )
    public static String commandKillItem = "false";

    @Rule(
            category = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"},
            desc = "Enables /tpt to teleport to another player in your server"
    )
    public static String commandTpt = "false";

    @Rule(
            category = {AYAKA, FEATURE},
            desc = "Disables natural spawning of bats"
    )
    public static boolean disableBatSpawning = false;

    @Rule(
            category = {AYAKA, EXPERIMENTAL, BOT},
            desc = "Forces fake players to spawn in offline mode",
            extra = "(1.16+) Only active when allowSpawningOfflinePlayers is set to true"
    )
    public static boolean fakePlayerForceOffline = false;

    @Rule(
            category = {AYAKA, EXPERIMENTAL, BUGFIX, BOT},
            desc = "Fixes the bug that fake players are not reconnected after retracements",
            extra = "Only active when Gca is loaded and fakePlayerResident is set to true"
    )
    public static boolean fakePlayerResidentBackupFix = false;

    @Rule(
            category = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE},
            desc = "Reintroduces the feature of cactuses, bamboos and sugarcane being (wrongly) random-ticked on scheduled ticks in Minecraft 1.15.2 and lower"
    )
    @Condition(ForceTickPlantsReintroduceCondition.class)
    public static boolean forceTickPlantsReintroduce = false;

    @Rule(
            category = {AYAKA, FEATURE},
            desc = "Prevents foxes from picking up dropped items"
    )
    public static boolean foxNoPickupItem = false;

    @Rule(
            category = {AYAKA, EXPERIMENTAL},
            desc = "Modifies the ticks before an item entity is discarded",
            extra = {"Set to 0 (or 6000) to use vanilla value", "Max value 72000 (an hour)"},
            validate = ItemDiscardAgeValidator.class,
            options = {"0", "3000", "3600", "6000", "12000", "72000"},
            strict = false
    )
    public static int itemDiscardAge = 0;

    @Rule(
            category = {AYAKA, COMMAND},
            desc = "Seconds to wait before clearing the items",
            validate = UnsignedIntegerValidator.class,
            options = {"0", "5", "10", "30"},
            strict = false
    )
    public static int killItemAwaitSeconds = 5;

    @Rule(
            category = {AYAKA, FEATURE, BUGFIX, EXPERIMENTAL, REINTRODUCE},
            desc = "Changes the way sliding velocity of non-living entities is calculated back to the original way in 1.21.1 and below",
            extra = "See MC-278572 and MC-275537"
    )
    @Condition(LegacyHoneyBlockSlidingCondition.class)
    public static boolean legacyHoneyBlockSliding = false;

    @Rule(
            category = {AYAKA, EXPERIMENTAL},
            desc = "Overwrites the max player count in a server",
            extra = {"Set to 0 to use vanilla value"},
            validate = UnsignedIntegerValidator.class,
            options = {"0", "8", "10", "20", "50", "100"},
            strict = false
    )
    public static int maxPlayersOverwrite = 0;

}
