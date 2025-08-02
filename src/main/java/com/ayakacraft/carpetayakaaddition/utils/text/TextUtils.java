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

package com.ayakacraft.carpetayakaaddition.utils.text;

import com.ayakacraft.carpetayakaaddition.utils.preprocess.PreprocessPattern;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public final class TextUtils {

    @PreprocessPattern
    private static MutableText literalText(String str) {
        //#if MC>=11900
        return Text.literal(str);
        //#else
        //$$ return new net.minecraft.text.LiteralText(str);
        //#endif
    }

    @PreprocessPattern
    private static MutableText translatableText(String key, Object... args) {
        //#if MC>=11900
        return Text.translatable(key, args);
        //#else
        //$$ return new net.minecraft.text.TranslatableText(key, args);
        //#endif
    }

    public static MutableText format(String str, Object... args) {
        return TextFormatter.format(str, args);
    }

    public static MutableText enter() {
        return Text.literal(System.lineSeparator());
    }

    public static MutableText space() {
        return Text.literal(" ");
    }

    public static MutableText empty() {
        return Text.literal("");
    }

    public static <T> Text join(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return net.minecraft.text.Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return empty();
        //$$ } else if (elements.size() == 1) {
        //$$     return transformer.apply(elements.iterator().next()).shallowCopy();
        //$$ } else {
        //$$     BaseText mutableText = empty();
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
        return join(elements, empty(), Function.identity());
    }

    public static Text joinTexts(Text[] elements) {
        return joinTexts(Arrays.asList(elements));
    }

    public static void sendMessageToServer(MinecraftServer server, Text text) {
        //#if MC>=11900
        server.sendMessage(text);
        //#elseif MC>=11600
        //$$ server.sendSystemMessage(text, null);
        //#else
        //$$ server.sendMessage(text);
        //#endif
    }

    public static void broadcast(MinecraftServer server, Text txt, boolean overlay) {
        sendMessageToServer(server, txt);
        server.getPlayerManager().getPlayerList().forEach(p -> p.sendMessage(txt, overlay));
    }

    public static void broadcast(MinecraftServer server, Text textForServer, Function<ServerPlayerEntity, Text> textFunction, boolean overlay) {
        sendMessageToServer(server, textForServer);
        server.getPlayerManager().getPlayerList().forEach(player -> {
            Text t = textFunction.apply(player);
            if (t == null) {
                return;
            }
            player.sendMessage(t, overlay);
        });
    }

    public static void broadcastTranslatable(MinecraftServer server, boolean overlay, Translator tr, Object... args) {
        broadcast(
                server,
                tr.tr(null, args),
                p -> tr.tr(p, null, args),
                overlay
        );
    }

    public static Style runCommand(Style style, String command) {
        return style.withColor(Formatting.GOLD)
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(command)));
    }

}
