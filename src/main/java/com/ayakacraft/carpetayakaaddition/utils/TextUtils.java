/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot
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

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public final class TextUtils {

    public static MutableText tr(String key, Object... args) {
        //#if MC>=11900
        return Text.translatable(key, args);
        //#else
        //$$ return new net.minecraft.text.TranslatableText(key, args);
        //#endif
    }

    public static MutableText li(String str, Object... args) {
        //#if MC>=11900
        return Text.literal(String.format(str, args));
        //#else
        //$$ return new net.minecraft.text.LiteralText(String.format(str, args));
        //#endif
    }

    public static Text of(String str, Object... args) {
        //#if MC>=11600
        return Text.of(String.format(str, args));
        //#else
        //$$ return li(str, args);
        //#endif
    }

    public static Text enter() {
        return of(System.lineSeparator());
    }

    public static <T> MutableText join(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return net.minecraft.text.Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return new net.minecraft.text.LiteralText("");
        //$$ } else if (elements.size() == 1) {
        //#endif
        //#if MC>=11700
        //#elseif MC>=11600
        //$$     return transformer.apply(elements.iterator().next()).shallowCopy();
        //#else
        //$$     return transformer.apply(elements.iterator().next()).deepCopy();
        //#endif
        //#if MC>=11700
        //#else
        //$$ } else {
        //$$     MutableText mutableText = new net.minecraft.text.LiteralText("");
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

    public static MutableText joinTexts(Collection<Text> elements) {
        return join(elements, of(""), Function.identity());
    }

    public static MutableText joinTexts(Text... elements) {
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
