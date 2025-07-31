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

package com.ayakacraft.carpetayakaaddition.utils.translation;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.utils.FileUtils;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.GSON;

public abstract class AyakaLanguage {

    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private static final Map<String, AyakaLanguage> languageMap = Maps.newHashMap();

    private static final String ASSETS = "assets/carpet-ayaka-addition/";

    private static final String LANG_META = ASSETS + "meta/languages.json";

    public static final String DEFAULT_LANG = "en_us";

    private static boolean loaded = false;

    public static void loadLanguages() {
        if (loaded) {
            return;
        }

        try {

            for (String lang : GSON.fromJson(FileUtils.readResource(LANG_META), String[].class)) {
                lang = lang.toLowerCase(Locale.ROOT);
                languageMap.put(lang, new AyakaLanguageImpl(lang));
            }

            loaded = true;
        } catch (Exception e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load language list", e);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                throw new RuntimeException(e);
            }
        }

    }

    public static AyakaLanguage get(String lang) {
        return languageMap.computeIfAbsent(lang.toLowerCase(Locale.ROOT), AyakaLanguageEmpty::new);
    }

    public static AyakaLanguage getDefaultLanguage() {
        return languageMap.get(DEFAULT_LANG);
    }

    public static AyakaLanguage getServerLanguage() {
        return get(
                //#if MC>=11900
                carpet.CarpetSettings.language
                //#elseif MC>=11500
                //$$ "none".equalsIgnoreCase(carpet.CarpetSettings.language) ? DEFAULT_LANG : carpet.CarpetSettings.language
                //#else
                //$$ DEFAULT_LANG
                //#endif
        );
    }

    private final String code;

    protected AyakaLanguage(String lang) {
        this.code = lang.toLowerCase(Locale.ROOT);
    }

    public String code() {
        return code;
    }

    public abstract Map<String, String> translations();

    public String translate(String key) {
        String s = translateWithoutFallback(key);
        if (s != null) {
            return s;
        }
        if (DEFAULT_LANG.equals(code)) {
            return key;
        }
        return getDefaultLanguage().translate(key);
    }

    public String translateWithoutFallback(String key) {
        return translations().get(key);
    }

    private static class AyakaLanguageImpl extends AyakaLanguage {

        private final Map<String, String> translations;

        private AyakaLanguageImpl(String code) {
            super(code);

            Map<String, String> tr = Collections.emptyMap();
            try {
                String              data = FileUtils.readResource(String.format("assets/carpet-ayaka-addition/lang/%s.json", code()));
                Map<String, String> map  = GSON.fromJson(data, MAP_TYPE);
                if (map != null) {
                    tr = Collections.unmodifiableMap(map);
                }
            } catch (Exception e) {
                CarpetAyakaAddition.LOGGER.error("Failed to load language {}", code(), e);
            }
            this.translations = tr;
        }

        @Override
        public Map<String, String> translations() {
            return translations;
        }

    }

    private static class AyakaLanguageEmpty extends AyakaLanguage {

        public AyakaLanguageEmpty(String code) {
            super(code);
        }

        @Override
        public Map<String, String> translations() {
            return Collections.emptyMap();
        }

    }

}
