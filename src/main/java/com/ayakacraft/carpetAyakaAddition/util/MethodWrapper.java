package com.ayakacraft.carpetAyakaAddition.util;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
//#if MC<11900
//$$ import net.minecraft.text.TranslatableText;
//#endif
import net.minecraft.text.Texts;
import net.minecraft.world.GameMode;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MethodWrapper {

    private static float wrapDegrees(float degrees) {
        float f = degrees % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static void sendFeedback(ServerCommandSource source, Supplier<Text> txt, boolean broadcastToOps) {
        //#if MC>=12000
        source.sendFeedback(txt, broadcastToOps);
        //#else
        //$$ source.sendFeedback(txt.get(), broadcastToOps);
        //#endif
    }

    public static boolean changeGameMode(ServerPlayerEntity player, GameMode gameMode) {
        //#if MC>=11700
        return player.changeGameMode(gameMode);
        //#else
        //$$ if(player.interactionManager.getGameMode() == gameMode) {
        //$$     return false;
        //$$ }
        //$$ player.setGameMode(gameMode);
        //$$ return true;
        //#endif
    }

    public static MutableText translatableText(String key, Object... args) {
        //#if MC>=11900
        return Text.translatable(key, args);
        //#else
        //$$ return new TranslatableText(key, args);
        //#endif
    }

    public static boolean isExecutedByPlayer(ServerCommandSource source) {
        //#if MC>=11900
        return source.isExecutedByPlayer();
        //#else
        //$$ return source.getEntity() instanceof ServerPlayerEntity;
        //#endif
    }

    public static float getYaw(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getYaw());
        //#else
        //$$ return wrapDegrees(player.getYaw(1f));
        //#endif
    }

    public static float getPitch(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getPitch());
        //#else
        //$$ return wrapDegrees(player.getPitch(1f));
        //#endif
    }

    public static MutableText literalText(String str, Object... args) {
        //#if MC>=11900
        return Text.literal(String.format(str, args));
        //#else
        //$$ return new net.minecraft.text.LiteralText(String.format(str, args));
        //#endif
    }

    public static ServerWorld getServerWorld(ServerPlayerEntity player) {
        //#if MC>=12000
        return player.getServerWorld();
        //#elseif MC>=11800
        //$$ return player.getWorld();
        //#else
        //$$ return player.getServerWorld();
        //#endif
    }

    public static <T> MutableText joinText(Collection<T> elements, Text separator, Function<T, Text> transformer) {
        //#if MC>=11700
        return Texts.join(elements, separator, transformer);
        //#else
        //$$ if (elements.isEmpty()) {
        //$$     return new net.minecraft.text.LiteralText("");
        //$$ } else if (elements.size() == 1) {
        //$$     return transformer.apply(elements.iterator().next()).copy();
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

}
