package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.server.command.CommandManager.literal;

public class GoHomeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("gohome")
                        .requires(source -> CommandUtil.checkPermission(source, CarpetAyakaSettings.commandGoHome, false))
                        .executes(commandContext -> {
                            final @NotNull ServerPlayerEntity player = commandContext.getSource().getPlayerOrThrow();
                            player.notInAnyWorld = true;
                            player.networkHandler.onClientStatus(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
                            return 1;
                        })
        );
    }

}
