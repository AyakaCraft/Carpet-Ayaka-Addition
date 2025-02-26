package com.ayakacraft.carpetAyakaAddition.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

    public static boolean checkPermission(ServerCommandSource source, boolean needNotOp, boolean needNotPlayer) {
        return (MethodWrapper.isExecutedByPlayer(source) || needNotPlayer) && (source.hasPermissionLevel(source.getServer().getOpPermissionLevel()) || needNotOp);
    }

    public static CompletableFuture<Suggestions> playerSuggestionProvider(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder);
    }

    public static CompletableFuture<Suggestions> vec3dSuggestionProvider(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder)
            throws CommandSyntaxException {
        final @NotNull ServerCommandSource source = context.getSource();
        if (MethodWrapper.isExecutedByPlayer(source)) {
            final @NotNull BlockPos pos = source.getPlayerOrThrow().getBlockPos();
            return CommandSource.suggestMatching(Arrays.asList(String.format("%.1f %.1f %.1f", pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)), builder);
        }
        return CommandSource.suggestMatching(new LinkedList<>(), builder);
    }

}
