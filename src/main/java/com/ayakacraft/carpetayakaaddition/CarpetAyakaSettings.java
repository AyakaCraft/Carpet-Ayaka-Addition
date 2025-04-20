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
    //$$ @Rule(category = {AYAKA, COMMAND}, desc = "Await seconds for killitem", validate = UnsignedIntegerValidator.class, options = {"0", "5", "10", "30"}, strict = false)
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

    //#if MC>=11900
    @Rule(categories = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE})
    //#else
    //$$ @Rule(category = {AYAKA, EXPERIMENTAL, REINTRODUCE, FEATURE}, desc = "0-tick force update plants")
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
