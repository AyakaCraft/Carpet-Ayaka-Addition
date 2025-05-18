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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class CarpetAyakaAddition implements ModInitializer {

    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private static final Map<String, Map<String, String>> translations = new HashMap<>(2);

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
        LOGGER = LogManager.getLogger(MOD_NAME);
    }

    public static Map<String, String> getTranslations(String lang) {
        if (translations.containsKey(lang)) {
            return translations.get(lang);
        }
        Map<String, String> translation = Collections.emptyMap();
        final InputStream   langStream  = CarpetAyakaServer.class.getClassLoader().getResourceAsStream(String.format("assets/carpet-ayaka-addition/lang/%s.json", lang));
        if (langStream != null) { // otherwise we don't have that language
            final String jsonData;
            try {
                byte[] data = new byte[langStream.available()];
                int    i    = langStream.read(data);
                if (i != data.length) {
                    data = Arrays.copyOf(data, i);
                }
                jsonData = new String(data, StandardCharsets.UTF_8);
                langStream.close();

                Map<String, String> map = GSON.fromJson(jsonData, MAP_TYPE);
                if (map != null) {
                    translation = map;
                    translations.put(lang, translation);
                }
            } catch (final IOException ignored) {
            }
        }

        return translation;
    }

    @Override
    public void onInitialize() {
        LOGGER.debug("Initializing {} Version {}", CarpetAyakaAddition.MOD_NAME, CarpetAyakaAddition.MOD_VERSION);
        CarpetServer.manageExtension(CarpetAyakaServer.INSTANCE);
    }

}
