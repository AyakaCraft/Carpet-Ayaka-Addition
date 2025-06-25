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

package com.ayakacraft.carpetayakaaddition.mixin.carpet;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.SettingsManager;
import com.ayakacraft.carpetayakaaddition.settings.conditions.AyakaCondition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.Condition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

@Mixin(value = SettingsManager.class, remap = false)
public class SettingsManagerMixin {

    @SuppressWarnings("rawtypes")
    @Unique
    private static final Constructor<ParsedRule> parsedRuleConstructor;

    static {
        try {
            parsedRuleConstructor = ParsedRule.class.getDeclaredConstructor(Field.class, Rule.class);
            parsedRuleConstructor.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Unique
    private static ParsedRule<?> of(Field field, Rule rule) {
        try {
            return parsedRuleConstructor.newInstance(field, rule);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Shadow
    private Map<String, ParsedRule<?>> rules;

    @WrapMethod(method = "parseSettingsClass")
    private void injectConditions(Class<?> settingsClass, Operation<Void> original) {

        rule:
        for (Field f : settingsClass.getDeclaredFields()) {
            Rule rule = f.getAnnotation(Rule.class);
            if (rule == null) {
                continue;
            }
            ParsedRule<?> parsed = of(f, rule);

            // Added part starts
            Condition conditionAnnotation = f.getAnnotation(Condition.class);
            if (conditionAnnotation != null) {
                for (Class<? extends AyakaCondition> condition : conditionAnnotation.value()) {
                    try {
                        Constructor<? extends AyakaCondition> constr = condition.getDeclaredConstructor();
                        constr.setAccessible(true);
                        if (!(constr.newInstance()).shouldRegister()) {
                            continue rule;
                        }
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
            // Added part ends

            rules.put(parsed.name, parsed);
        }

    }

}
