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

import carpet.api.settings.Rule;
import com.ayakacraft.carpetayakaaddition.settings.conditions.Condition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.ForceTickPlantsReintroduceCondition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.LegacyHoneyBlockSlidingCondition;
import com.ayakacraft.carpetayakaaddition.settings.validators.ItemDiscardAgeValidator;
import com.ayakacraft.carpetayakaaddition.settings.validators.UnsignedIntegerValidator;

import static carpet.api.settings.RuleCategory.*;

//Do not remove the lines below
//TODO update in 1.18.2
//TODO update translation
public final class CarpetAyakaSettings {

    public static final String AYAKA = "Ayaka";

    public static final String REINTRODUCE = "reintroduce";

    public static final String CHEAT = "cheat";

    public static final String BOT = "BOT";

    @Rule(
            categories = {AYAKA, SURVIVAL, CHEAT}
    )
    public static boolean betterOpPlayerNoCheat = false;

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"}
    )
    public static String commandAddress = "false";

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"}
    )
    public static String commandC = "false";

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"}
    )
    public static String commandGoHome = "false";

    @Rule(
            categories = {AYAKA, COMMAND},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"}
    )
    public static String commandKillItem = "false";

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT},
            options = {"false", "true", "ops", "0", "1", "2", "3", "4"}
    )
    public static String commandTpt = "false";

    @Rule(
            categories = {AYAKA, FEATURE}
    )
    public static boolean disableBatSpawning = false;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL, BOT}
    )
    public static boolean fakePlayerForceOffline = false;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL, BUGFIX, BOT}
    )
    public static boolean fakePlayerResidentBackupFix = false;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE}
    )
    @Condition(ForceTickPlantsReintroduceCondition.class)
    public static boolean forceTickPlantsReintroduce = false;

    @Rule(
            categories = {AYAKA, FEATURE}
    )
    public static boolean foxNoPickupItem = false;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL},
            validators = ItemDiscardAgeValidator.class,
            options = {"0", "3000", "3600", "6000", "12000", "72000"},
            strict = false
    )
    public static int itemDiscardAge = 0;

    @Rule(
            categories = {AYAKA, COMMAND},
            validators = UnsignedIntegerValidator.class,
            options = {"0", "5", "10", "30"},
            strict = false
    )
    public static int killItemAwaitSeconds = 5;

    @Rule(
            categories = {AYAKA, FEATURE, BUGFIX, EXPERIMENTAL, REINTRODUCE}
    )
    @Condition(LegacyHoneyBlockSlidingCondition.class)
    public static boolean legacyHoneyBlockSliding = false;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL},
            validators = UnsignedIntegerValidator.class,
            options = {"0", "8", "10", "20", "50", "100"},
            strict = false
    )
    public static int maxPlayersOverwrite = 0;

}
