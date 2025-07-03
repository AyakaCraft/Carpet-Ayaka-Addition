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

import carpet.CarpetServer;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class CarpetAyakaAddition implements ModInitializer {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static final String MOD_ID = "carpet-ayaka-addition";

    public static final String MOD_NAME;

    public static final Logger LOGGER;

    public static final String MOD_VERSION;

    static {
        final Optional<ModContainer> o = ModUtils.getModContainer(MOD_ID);
        if (o.isPresent()) {
            MOD_NAME = o.get().getMetadata().getName();
            MOD_VERSION = o.get().getMetadata().getVersion().toString();
        } else {
            MOD_NAME = "Carpet Ayaka Addition";
            MOD_VERSION = "dev";
        }
        LOGGER = LoggerFactory.getLogger(MOD_NAME);
    }

    @Override
    public void onInitialize() {
        LOGGER.debug("Initializing {} Version {}", CarpetAyakaAddition.MOD_NAME, CarpetAyakaAddition.MOD_VERSION);
        AyakaLanguage.loadLanguages();
        CarpetServer.manageExtension(CarpetAyakaServer.INSTANCE);
    }

}
