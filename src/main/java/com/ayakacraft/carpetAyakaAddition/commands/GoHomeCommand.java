package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import lombok.val;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class GoHomeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("gohome")
                        .requires(source -> CommandUtil.checkPermissioin(source, CarpetAyakaSettings.commandGoHome, false))
                        .executes(commandContext -> {
                            val self = commandContext.getSource().getPlayer();
//                            if (self == null) {
//                                return 0;
//                            }
                            self.notInAnyWorld = true;
                            self.networkHandler.onClientStatus(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
//                            var p2=source.getServer().getPlayerManager().respawnPlayer(self,true);
//                            var server = source.getServer();
//                            var   spawnWorld    = server.getWorld(self.getSpawnPointDimension());
//                            var   spawnBlockPos = self.getSpawnPointPosition();
//                            Vec3d spawnPos;
//                            if (spawnBlockPos != null && spawnWorld != null) {
//                                spawnPos = PlayerEntity.findRespawnPosition(
//                                        spawnWorld,
//                                        self.getSpawnPointPosition(),
//                                        self.getSpawnAngle(),
//                                        true,
//                                        true
//                                ).get();
//                            } else {
//                                spawnPos = PlayerEntity.findRespawnPosition(
//                                        server.getOverworld(),
//                                        server.getOverworld().getSpawnPos(),
//                                        server.getOverworld().getSpawnAngle(),
//                                        true,
//                                        true
//                                ).get();
//                            }
//                            self.teleport(spawnWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0f, 0f);
                            return 1;
                        })
        );
    }

}
