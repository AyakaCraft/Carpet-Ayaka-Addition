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
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public final class TextUtils {

    @PreprocessPattern
    private static MutableComponent literalText(String str) {
        //#if MC>=11900
        return Component.literal(str);
        //#else
        //$$ return new net.minecraft.network.chat.TextComponent(str);
        //#endif
    }

    @PreprocessPattern
    private static MutableComponent translatableText(String key, Object... args) {
        //#if MC>=11900
        return Component.translatable(key, args);
        //#else
        //$$ return new net.minecraft.network.chat.TranslatableComponent(key, args);
        //#endif
    }

    @Contract(pure = true)
    public static MutableComponent format(String str, Object... args) {
        return TextFormatter.format(str, args);
    }

    @Contract(pure = true)
    public static MutableComponent enter() {
        return Component.literal(System.lineSeparator());
    }

    @Contract(pure = true)
    public static MutableComponent space() {
        return Component.literal(" ");
    }

    @Contract(pure = true)
    public static MutableComponent empty() {
        return Component.literal("");
    }

    @Contract(pure = true)
    public static <T> Component join(Collection<T> elements, Component separator, Function<T, Component> transformer) {
        //#if MC>=11700
        return net.minecraft.network.chat.ComponentUtils.formatList(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return empty();
        //$$ } else if (elements.size() == 1) {
        //$$     return transformer.apply(elements.iterator().next()).copy();
        //$$ } else {
        //$$     BaseComponent mutableText = empty();
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

    @Contract(pure = true)
    public static Component joinTexts(Collection<Component> elements) {
        return join(elements, empty(), Function.identity());
    }

    @Contract(pure = true)
    public static Component joinTexts(Component[] elements) {
        return joinTexts(Arrays.asList(elements));
    }

    public static void sendMessageToServer(MinecraftServer server, Component text) {
        //#if MC>=11900
        server.sendSystemMessage(text);
        //#elseif MC>=11600
        //$$ server.sendMessage(text, null);
        //#else
        //$$ server.sendMessage(text);
        //#endif
    }

    public static void broadcast(MinecraftServer server, Component txt, boolean overlay) {
        sendMessageToServer(server, txt);
        server.getPlayerList().getPlayers().forEach(p -> p.displayClientMessage(txt, overlay));
    }

    public static void broadcast(MinecraftServer server, Component textForServer, Function<ServerPlayer, Component> textFunction, boolean overlay) {
        sendMessageToServer(server, textForServer);
        server.getPlayerList().getPlayers().forEach(player -> {
            Component t = textFunction.apply(player);
            if (t != null) {
                player.displayClientMessage(t, overlay);
            }
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

    @Contract(mutates = "param1")
    public static MutableComponent withCommand(MutableComponent text, String command) {
        text.withStyle(style ->
                style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(command))));
        return text;
    }

}
