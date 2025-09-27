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
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
import com.ayakacraft.carpetayakaaddition.utils.StringUtils;
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public class Translator {

    public static final Translator ROOT = new Translator();

    public static final Translator AYAKA = new Translator(CarpetAyakaAddition.MOD_ID);

    public static final Translator CARPET = new Translator(ModUtils.CPT_ID);

    private final String baseKey;

    public Translator() {
        this.baseKey = null;
    }

    public Translator(String baseKey) {
        this.baseKey = baseKey;
    }

    public Translator resolve(String key) {
        if (StringUtils.isNullOrEmpty(baseKey)) {
            return new Translator(key);
        }
        return new Translator(this.baseKey + "." + key);
    }

    public String translate(AyakaLanguage lang, String key) {
        String k;
        if (StringUtils.isNullOrEmpty(baseKey)) {
            if (StringUtils.isNullOrEmpty(key)) {
                k = "";
            } else {
                k = key;
            }
        } else {
            if (StringUtils.isNullOrEmpty(key)) {
                k = baseKey;
            } else {
                k = baseKey + "." + key;
            }
        }
        return lang.translate(k);
    }

    public String translateWithoutFallback(AyakaLanguage lang, String key) {
        String k;
        if (StringUtils.isNullOrEmpty(baseKey)) {
            if (StringUtils.isNullOrEmpty(key)) {
                k = "";
            } else {
                k = key;
            }
        } else {
            if (StringUtils.isNullOrEmpty(key)) {
                k = baseKey;
            } else {
                k = baseKey + "." + key;
            }
        }
        return lang.translateWithoutFallback(k);
    }

    public MutableText tr(String key, Object... args) {
        return tr(AyakaLanguage.getServerLanguage(), key, args);
    }

    public MutableText tr(AyakaLanguage lang, String key, Object... args) {
        return TextUtils.format(translate(lang, key), args);
    }

    public MutableText tr(ServerPlayerEntity player, String key, Object... args) {
        return tr(ServerPlayerUtils.getLanguage(player), key, args);
    }

    public MutableText tr(ServerCommandSource source, String key, Object... args) {
        return tr(CommandUtils.getLanguage(source), key, args);
    }

    public Supplier<MutableText> trS(ServerCommandSource source, String key, Object... args) {
        return () -> tr(source, key, args);
    }

}
