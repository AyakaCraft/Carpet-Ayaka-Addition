package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.util.CommandUtils;
import com.ayakacraft.carpetAyakaAddition.util.MethodWrapper;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.server.command.CommandManager.literal;

public final class CCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("c")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandC, false))
                        .executes(ctx -> {
                            final ServerCommandSource         source = ctx.getSource();
                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();
                            if (!MethodWrapper.changeGameMode(player, GameMode.SURVIVAL)) {
                                MethodWrapper.changeGameMode(player, GameMode.SPECTATOR);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
    }

}
