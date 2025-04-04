package com.ayakacraft.carpetayakaaddition.commands;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
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
                        .requires(source -> CommandUtils.checkPermission(source, !CarpetAyakaSettings.commandC, true))
                        .executes(ctx -> {
                            final ServerCommandSource         source = ctx.getSource();
                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();
                            if (!ServerPlayerUtils.changeGameMode(player, GameMode.SURVIVAL)) {
                                ServerPlayerUtils.changeGameMode(player, GameMode.SPECTATOR);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
    }

}
