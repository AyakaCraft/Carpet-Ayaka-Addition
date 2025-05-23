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

package com.ayakacraft.carpetayakaaddition.utils;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.GSON;

public final class AyakaLanguage {

    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private static final Type STRING_ARRAY_TYPE = new TypeToken<String[]>() {
    }.getType();

    private static final Map<String, AyakaLanguage> languageMap = new HashMap<>(2);

    private static final String ASSETS = "assets/carpet-ayaka-addition/";

    private static final String LANG_META = ASSETS + "meta/languages.json";

    public static final String DEFAULT_LANG = "en_us";

    private static boolean loaded = false;

    public static void loadLanguages() {
        if (loaded) {
            return;
        }

        try {
            final String data      = FileUtils.readResource(LANG_META);
            String[]     languages = GSON.fromJson(data, STRING_ARRAY_TYPE);

            for (String lang : languages) {
                lang = lang.toLowerCase();
                languageMap.put(lang, new AyakaLanguage(lang));
            }

            loaded = true;
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load language list", e);
        }

    }

    public static AyakaLanguage getServerLanguage() {
        return getOrEnglish(
                //#if MC>=11900
                carpet.CarpetSettings.language
                //#elseif MC>=11500
                //$$ "none".equalsIgnoreCase(carpet.CarpetSettings.language) ? DEFAULT_LANG : carpet.CarpetSettings.language
                //#else
                //$$ DEFAULT_LANG
                //#endif
        );
    }

    public static AyakaLanguage getOrEnglish(String lang) {
        lang = lang.toLowerCase();
        if (languageMap.containsKey(lang)) {
            return languageMap.get(lang);
        } else {
            return languageMap.get(DEFAULT_LANG);
        }
    }

    public static AyakaLanguage getOrServer(String lang) {
        lang = lang.toLowerCase();
        if (languageMap.containsKey(lang)) {
            return languageMap.get(lang);
        } else {
            return getServerLanguage();
        }
    }

    @Nullable
    public static AyakaLanguage get(String lang) {
        return languageMap.get(lang.toLowerCase());
    }

    private final Map<String, String> translations;

    private final String code;

    private AyakaLanguage(String lang) {
        this.code = lang;

        Map<String, String> tr = Collections.emptyMap();
        try {
            final String        data = FileUtils.readResource(String.format("assets/carpet-ayaka-addition/lang/%s.json", lang));
            Map<String, String> map  = GSON.fromJson(data, MAP_TYPE);
            if (map != null) {
                tr = map;
            }
        } catch (Exception e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load language {}", lang, e);
        }
        this.translations = tr;
    }

    public Map<String, String> translations() {
        return Collections.unmodifiableMap(translations);
    }

    public String translate(String key) {
        return translations.getOrDefault(key, key);
    }

    public String code() {
        return code;
    }

}
