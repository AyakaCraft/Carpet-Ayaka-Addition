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

package com.ayakacraft.carpetayakaaddition.mixin.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.reflect.Field;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<1.19"))
@Mixin(ParsedRule.class)
public interface ParsedRuleAccessor {

    @Invoker("<init>")
    static ParsedRule<?> invokeConstructor(
            Field field, Rule rule
            //#if MC>=11600
            , carpet.settings.SettingsManager settingsManager
            //#endif
    ) {
        throw new AssertionError();
    }

}
