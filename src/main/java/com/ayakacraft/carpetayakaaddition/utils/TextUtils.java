package com.ayakacraft.carpetayakaaddition.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

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

    public static Text enter() {
        return Text.of(System.lineSeparator());
    }

    public static <T> MutableText join(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return new net.minecraft.text.LiteralText("");
        //$$ } else if (elements.size() == 1) {
        //$$     return transformer.apply(elements.iterator().next()).shallowCopy();
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
        return join(elements, Text.of(""), Function.identity());
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
