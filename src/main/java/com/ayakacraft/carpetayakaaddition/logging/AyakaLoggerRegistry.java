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

package com.ayakacraft.carpetayakaaddition.logging;

import carpet.logging.HUDController;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import com.ayakacraft.carpetayakaaddition.logging.loadedchunks.LoadedChunksLogger;

import java.util.HashSet;
import java.util.Set;

public class AyakaLoggerRegistry {

    public static boolean __loadedChunks = false;

    public static final Set<Logger> ayakaLoggers = new HashSet<>();

    static {
        ayakaLoggers.add(LoadedChunksLogger.INSTANCE);
    }

    public static void registerLoggers() {
        //#if MC>=11500
        ayakaLoggers.forEach(it -> LoggerRegistry.registerLogger(it.getLogName(), it));
        //#endif

        //#if MC>=11600
        HUDController.register(minecraftServer -> updateHUD()); // We use mixin to deal with 1.14 and 1.15
        //#endif
    }

    public static void updateHUD() {
        if (__loadedChunks) {
            LoadedChunksLogger.INSTANCE.doLogging();
        }
    }

}
