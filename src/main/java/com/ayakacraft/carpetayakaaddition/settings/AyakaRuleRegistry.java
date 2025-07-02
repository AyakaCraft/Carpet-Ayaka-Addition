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

import carpet.CarpetServer;
import carpet.api.settings.CarpetRule;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.mixin.carpet.SettingsManagerAccessor;
import com.ayakacraft.carpetayakaaddition.settings.conditions.AyakaCondition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

public final class AyakaRuleRegistry {

    private static Map<String, CarpetRule<?>> getRulesMap() {
        return ((SettingsManagerAccessor) CarpetServer.settingsManager).getRules$Ayaka();
    }

    public static void registerRules() {
        Field[] fields = CarpetAyakaSettings.class.getDeclaredFields();

        rule:
        for (Field field : fields) {
            field.setAccessible(true);

            Rule annotation = field.getAnnotation(Rule.class);
            if (annotation == null) {
                continue;
            }

            Class<? extends AyakaCondition>[] conditions = annotation.conditions();
            for (Class<? extends AyakaCondition> condition : conditions) {
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

            AyakaRule ayakaRule = new AyakaRule(field, CarpetServer.settingsManager);
            //#if MC>=11900
            CarpetServer.settingsManager.addCarpetRule(ayakaRule.constructCarpetRule());
            //#else
            //$$ getRulesMap().put(field.getName(), ayakaRule.constructCarpetRule());
            //#endif

        }
    }

}
