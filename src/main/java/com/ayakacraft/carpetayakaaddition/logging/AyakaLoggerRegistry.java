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

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import com.ayakacraft.carpetayakaaddition.logging.loadedchunks.LoadedChunksLogger;
import com.ayakacraft.carpetayakaaddition.logging.movingblocks.MovingBlocksLogger;
import com.ayakacraft.carpetayakaaddition.logging.poi.POILogger;
import com.ayakacraft.carpetayakaaddition.mixin.logging.LoggerRegistryMixin;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.google.common.collect.Sets;

import java.util.Set;

public final class AyakaLoggerRegistry {

    public static final Translator LOGGER_TR = Translator.AYAKA.resolve("logger");

    public static final Set<Logger> ayakaLoggers = Sets.newHashSet();

    public static final Set<AbstractAyakaHUDLogger> ayakaHUDLoggers = Sets.newHashSet();

    public static boolean __loadedChunks = false;

    public static boolean __movingBlocks = false;

    public static boolean __poi = false;

    static {
        registerAyakaLogger(LoadedChunksLogger.INSTANCE);
        registerAyakaLogger(MovingBlocksLogger.INSTANCE);
        registerAyakaLogger(POILogger.INSTANCE);
    }

    public static void registerAyakaLogger(AbstractAyakaLogger logger) {
        ayakaLoggers.add(logger);
    }

    public static void registerAyakaLogger(AbstractAyakaHUDLogger logger) {
        ayakaHUDLoggers.add(logger);
        ayakaLoggers.add(logger);
    }

    //#if MC>=11500
    /**
     * @see LoggerRegistryMixin
     */
    public static void registerToCarpet() {
        ayakaLoggers.forEach(it -> LoggerRegistry.registerLogger(it.getLogName(), it));
    }
    //#endif

    public static void updateHUD() {
        ayakaHUDLoggers.stream().filter(AyakaExtensionHUDLogger::isEnabled).forEach(AyakaExtensionHUDLogger::doLogging);
    }

}
