package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.server.command.CommandManager.literal;

public class CGameModeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("cgamemode")
                        .requires(source -> CommandUtil.checkPermission(source, CarpetAyakaSettings.commandGoHome, false))
                        .executes(ctx -> {
                            final ServerCommandSource source = ctx.getSource();
                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();
                            if(!player.changeGameMode(GameMode.SURVIVAL)) player.changeGameMode(GameMode.SPECTATOR);
                            return Command.SINGLE_SUCCESS;
                        }));
    }

}
