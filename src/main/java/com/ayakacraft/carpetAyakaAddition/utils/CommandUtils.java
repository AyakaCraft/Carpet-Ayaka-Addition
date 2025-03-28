package com.ayakacraft.carpetayakaaddition.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

    public static boolean checkPermission(ServerCommandSource source, boolean needsOp, boolean needsPlayer) {
        return (isExecutedByPlayer(source) || !needsPlayer) && (source.hasPermissionLevel(source.getServer().getOpPermissionLevel()) || !needsOp);
    }

    public static CompletableFuture<Suggestions> playerSuggestionProvider(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder);
    }

    public static void sendFeedback(ServerCommandSource source, Text txt, boolean broadcastToOps) {
        //#if MC>=12000
        source.sendFeedback(() -> txt, broadcastToOps);
        //#else
        //$$ source.sendFeedback(txt, broadcastToOps);
        //#endif
    }

    public static boolean isExecutedByPlayer(ServerCommandSource source) {
        //#if MC>=11900
        return source.isExecutedByPlayer();
        //#else
        //$$ return source.getEntity() instanceof net.minecraft.server.network.ServerPlayerEntity;
        //#endif
    }

}
