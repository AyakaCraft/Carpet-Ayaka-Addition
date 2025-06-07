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

import java.net.Proxy;

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
            categories = {AYAKA, EXPERIMENTAL}
    )
    public static Proxy.Type authProxyType = Proxy.Type.DIRECT;

    @Rule(
            categories = {AYAKA, EXPERIMENTAL},
            options = "127.0.0.1",
            strict = false
    )
    public static String authProxyHost = "127.0.0.1";

    @Rule(
            categories = {AYAKA, EXPERIMENTAL},
            validators = AddressPortValidator.class,
            options = "7897",
            strict = false
    )
    public static int authProxyPort = 7897;

    @Rule(
            categories = {AYAKA, SURVIVAL, CHEAT}
    )
    public static boolean betterOpPlayerNoCheat = false;

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT}
    )
    public static boolean commandAddress = false;

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT}
    )
    public static boolean commandC = false;

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT}
    )
    public static boolean commandGoHome = false;

    @Rule(
            categories = {AYAKA, COMMAND}
    )
    public static boolean commandKillItem = false;

    @Rule(
            categories = {AYAKA, COMMAND, CHEAT}
    )
    public static boolean commandTpt = false;

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
            categories = {AYAKA, EXPERIMENTAL},
            validators = UnsignedIntegerValidator.class,
            options = {"0", "8", "10", "20", "50", "100"},
            strict = false
    )
    public static int maxPlayersOverwrite = 0;


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

    private static final class AddressPortValidator extends Validator<Integer> {

        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            if (newValue < 0 || newValue > 65535) {
                return null;
            }
            return newValue;
        }

    }

}
