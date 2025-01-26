package com.ayakacraft.carpetAyakaAddition.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.val;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CommandUtil {

    public static boolean checkPermission(ServerCommandSource source, boolean needNotOp, boolean needNotPlayer) {
        return (source.isExecutedByPlayer() || needNotPlayer) && (Objects.requireNonNull(source.getPlayer()).hasPermissionLevel(4) || needNotOp);
    }

    public static CompletableFuture<Suggestions> playerSuggestionProvider(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder);
    }

    public static CompletableFuture<Suggestions> vec3dSuggestionProvider(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        final @NotNull ServerCommandSource source = context.getSource();
        if (source.isExecutedByPlayer()) {
            final @NotNull BlockPos pos = source.getPlayerOrThrow().getBlockPos();
            String str = "%.1f %.1f %.1f".formatted(pos.getX() + 0.5, pos.getY()+0.5, pos.getZ() + 0.5);
            return CommandSource.suggestMatching(List.of(str), builder);
        }
        return CommandSource.suggestMatching(List.of(), builder);
    }

}
