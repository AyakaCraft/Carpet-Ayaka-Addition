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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public final class TextUtils {

    @SuppressWarnings("unused")
    public static final String DEFAULT_LANG = "en_us";

    public static MutableText trServer(String key, Object... args) {
        //#if MC>=11900
        return trLang(carpet.CarpetSettings.language, key, args);
        //#elseif MC>=11500
        //$$ return trLang(
        //$$         "none".equalsIgnoreCase(carpet.CarpetSettings.language) ? DEFAULT_LANG : carpet.CarpetSettings.language
        //$$         , key, args
        //$$ );
        //#else
        //$$ return trLang(DEFAULT_LANG, key, args);
        //#endif
    }

    public static MutableText trLang(String lang, String key, Object... args) {
        return li(CarpetAyakaAddition.getTranslations(lang.toLowerCase()).get(key), args);
    }

    public static MutableText tr(ServerPlayerEntity player, String key, Object... args) {
        return trLang(player.getClientOptions().language(), key, args);
    }

    public static MutableText tr(ServerCommandSource source, String key, Object... args) {
        if (source.isExecutedByPlayer()) {
            return tr((ServerPlayerEntity) source.getEntity(), key, args);
        } else {
            return trServer(key, args);
        }
    }

    public static MutableText li(String str, Object... args) {
        //#if MC>=11900
        return Text.literal(String.format(str, args));
        //#else
        //$$ return new net.minecraft.text.LiteralText(String.format(str, args));
        //#endif
    }

    @Deprecated
    public static MutableText of(String str, Object... args) {
        return li(str, args);
    }

    public static MutableText enter() {
        return li(System.lineSeparator());
    }

    public static MutableText space() {
        return li(" ");
    }

    public static MutableText empty() {
        return li("");
    }

    public static <T> Text join(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return net.minecraft.text.Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return new net.minecraft.text.LiteralText("");
        //$$ } else if (elements.size() == 1) {
        //$$ //#if MC>=11600
        //$$     return transformer.apply(elements.iterator().next()).shallowCopy();
        //$$ //#else
        //$$ //$$     return transformer.apply(elements.iterator().next()).deepCopy();
        //$$ //#endif
        //$$ } else {
        //$$     net.minecraft.text.BaseText mutableText = new net.minecraft.text.LiteralText("");
        //$$     boolean bl = true;
        //$$
        //$$     for(T object : elements) {
        //$$         if (!bl) {
        //$$             mutableText.append(separator);
        //$$         }
        //$$         mutableText.append(transformer.apply(object));
        //$$         bl = false;
        //$$     }
        //$$
        //$$     return mutableText;
        //$$ }
        //#endif
    }

    public static Text joinTexts(Collection<Text> elements) {
        return join(elements, of(""), Function.identity());
    }

    public static Text joinTexts(Text... elements) {
        return joinTexts(Arrays.asList(elements));
    }

    public static void broadcastToPlayers(MinecraftServer server, Text text, boolean overlay) {
        final PlayerManager playerManager = server.getPlayerManager();
        //#if MC>=11900
        playerManager.broadcast(text, overlay);
        //#else
        //$$ playerManager.getPlayerList().forEach(player -> player.sendMessage(text, overlay));
        //#endif
    }

}
