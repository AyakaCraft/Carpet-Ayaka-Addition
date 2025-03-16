package com.ayakacraft.carpetayakaaddition.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import org.intellij.lang.annotations.Pattern;

import java.util.Collection;
import java.util.function.Function;

public final class TextUtils {

    public static MutableText translatableText(String key, Object... args) {
        //#if MC>=11900
        return Text.translatable(key, args);
        //#else
        //$$ return new TranslatableText(key, args);
        //#endif
    }

    public static MutableText literalText(String str, Object... args) {
        //#if MC>=11900
        return Text.literal(String.format(str, args));
        //#else
        //$$ return new LiteralText(String.format(str, args));
        //#endif
    }

    public static <T> MutableText join(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return net.minecraft.text.Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return new LiteralText("");
        //$$ } else if (elements.size() == 1) {
        //$$     return transformer.apply(elements.iterator().next()).shallowCopy();
        //$$ } else {
        //$$     MutableText mutableText = new LiteralText("");
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

    public static void broadcastToPlayers(MinecraftServer server, Text text, boolean overlay) {
        final PlayerManager playerManager = server.getPlayerManager();
        //#if MC>=11900
        playerManager.broadcast(text, overlay);
        //#else
        //$$ playerManager.getPlayerList().forEach(player -> player.sendMessage(text, overlay));
        //#endif
    }

}
