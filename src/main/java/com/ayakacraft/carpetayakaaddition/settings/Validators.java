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

package com.ayakacraft.carpetayakaaddition.settings;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public final class Validators {

    public static class ItemDiscardAgeValidator extends Validator<Integer> {

        public static final int ITEM_DISCARD_AGE_MAX_VALUE = 72000;

        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return (newValue < 0 || newValue > ITEM_DISCARD_AGE_MAX_VALUE) ? null : newValue;
        }

        @Override
        public String description() {
            return String.format(
                    AyakaLanguage.getServerLanguage().translate("carpet.validator.invalid_desc.unsigned_with_bound"),
                    ITEM_DISCARD_AGE_MAX_VALUE
            );
        }

    }

    public static class UnsignedDoubleValidator extends Validator<Double> {

        @Override
        public Double validate(@Nullable ServerCommandSource source, CarpetRule<Double> changingRule, Double newValue, String userInput) {
            return newValue < 0 ? null : newValue;
        }

        @Override
        public String description() {
            return AyakaLanguage.getServerLanguage().translate("carpet.validator.invalid_desc.unsigned");
        }

    }

    public static class UnsignedIntegerValidator extends Validator<Integer> {

        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return newValue < 0 ? null : newValue;
        }

        @Override
        public String description() {
            return AyakaLanguage.getServerLanguage().translate("carpet.validator.invalid_desc.unsigned");
        }

    }

}
