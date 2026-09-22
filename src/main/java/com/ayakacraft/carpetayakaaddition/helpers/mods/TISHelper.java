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

package com.ayakacraft.carpetayakaaddition.helpers.mods;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Method;

public final class TISHelper {

    private static final Class<?> tisUpdateSuppressionExceptionClass;

    private static final Method canCheatMethod;

    static {
        Class<?> updateSupressionEx = null;
        try {
            updateSupressionEx = TISHelper.class.getClassLoader().loadClass("carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException");
        } catch (ClassNotFoundException ignored) {
        }
        tisUpdateSuppressionExceptionClass = updateSupressionEx;

        Method h = null;
        try {
            Class<?> clazz = TISHelper.class.getClassLoader().loadClass("carpettisaddition.helpers.rule.opPlayerNoCheat.OpPlayerNoCheatHelper");
            h = clazz.getMethod("canCheat", CommandSourceStack.class);
        } catch (Throwable ignored) {
        }
        canCheatMethod = h;
    }

    @Contract(pure = true)
    public static boolean isTisUpdateSuppressionException(Object o) {
        return tisUpdateSuppressionExceptionClass != null && tisUpdateSuppressionExceptionClass.isAssignableFrom(o.getClass());
    }

    @Contract(pure = true)
    public static boolean canCheat(CommandSourceStack source) {
        if (!CarpetAyakaSettings.betterOpPlayerNoCheat || canCheatMethod == null) {
            return true;
        }
        try {
            return (Boolean) canCheatMethod.invoke(null, source);
        } catch (Throwable t) {
            return true;
        }
    }

}
