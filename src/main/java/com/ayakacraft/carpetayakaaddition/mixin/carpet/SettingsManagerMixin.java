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

import carpet.CarpetSettings;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import com.ayakacraft.carpetayakaaddition.settings.conditions.AyakaCondition;
import com.ayakacraft.carpetayakaaddition.settings.conditions.Condition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

//Do not remove the lines below
//TODO update in 1.14.4 and 1.18.2
@Mixin(value = SettingsManager.class, remap = false)
public class SettingsManagerMixin {

    @Shadow
    @Final
    private Map<String, CarpetRule<?>> rules;

    @SuppressWarnings("removal")
    @WrapMethod(method = "parseSettingsClass")
    private void injectConditions(Class<?> settingsClass, Operation<Void> original) {

        // In the current translation system languages are not loaded this early. Ensure they are loaded
        Translations.updateLanguage();
        boolean warned = settingsClass == CarpetSettings.class; // don't warn for ourselves

        nextRule:
        for (Field field : settingsClass.getDeclaredFields()) {
            Class<? extends Rule.Condition>[] conditions;
            Rule                              newAnnotation = field.getAnnotation(Rule.class);
            carpet.settings.Rule              oldAnnotation = field.getAnnotation(carpet.settings.Rule.class);
            if (newAnnotation != null) {
                conditions = newAnnotation.conditions();
            } else if (oldAnnotation != null) {
                conditions = oldAnnotation.condition();
                if (!warned) {
                    CarpetSettings.LOG.warn(
                            """
                                    Registering outdated rules for settings class '{}'!
                                    This won't be supported in the future and rules won't be registered!""",
                            settingsClass.getName()
                    );
                    warned = true;
                }
            } else {
                continue;
            }
            for (Class<? extends Rule.Condition> condition : conditions) { //Should this be moved to ParsedRule.of?
                try {
                    Constructor<? extends Rule.Condition> constr = condition.getDeclaredConstructor();
                    constr.setAccessible(true);
                    if (!(constr.newInstance()).shouldRegister()) {
                        continue nextRule;
                    }
                } catch (ReflectiveOperationException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            // Added part starts
            Condition conditionAnnotation = field.getAnnotation(Condition.class);
            if (conditionAnnotation != null) {
                for (Class<? extends AyakaCondition> condition : conditionAnnotation.value()) {
                    try {
                        Constructor<? extends AyakaCondition> constr = condition.getDeclaredConstructor();
                        constr.setAccessible(true);
                        if (!(constr.newInstance()).shouldRegister()) {
                            continue nextRule;
                        }
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
            // Added part ends

            CarpetRule<?> parsed = carpet.settings.ParsedRule.of(field, (SettingsManager) (Object) this);
            rules.put(parsed.name(), parsed);
        }
    }

}
