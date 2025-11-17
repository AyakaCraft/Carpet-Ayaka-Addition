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
import com.ayakacraft.carpetayakaaddition.mixin.settings.SettingsManagerAccessor;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public final class AyakaRuleRegistry {

    private static Map<String, CarpetRule<?>> getRulesMap() {
        return ((SettingsManagerAccessor) CarpetServer.settingsManager).getRules$Ayaka();
    }

    private static boolean isSatisfied(ModCondition modCondition) {
        return
                ModUtils.isModLoadedWithVersion(modCondition.value(), modCondition.versionPredicates())
                        == (modCondition.type() == ModCondition.Type.REQUIREMENT);
    }

    public static void registerRules() {
        Field[] fields = CarpetAyakaSettings.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Rule annotation = field.getAnnotation(Rule.class);
            if (annotation == null) {
                continue;
            }

            ModCondition[] modConditions = annotation.modConditions();
            if (modConditions.length > 0 && !Arrays.stream(annotation.modConditions()).allMatch(AyakaRuleRegistry::isSatisfied)) {
                continue;
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
