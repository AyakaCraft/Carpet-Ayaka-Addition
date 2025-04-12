package com.ayakacraft.carpetayakaaddition.commands;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.literal;

public final class GoHomeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("gohome")
                        .requires(source -> CommandUtils.checkPermission(source, !CarpetAyakaSettings.commandGoHome, true))
                        .executes(context -> {
                            final ServerCommandSource         source = context.getSource();
                            final MinecraftServer             server = source.getServer();
                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();

                            final Collection<StatusEffectInstance> c = player.getStatusEffects();

                            final ServerPlayerEntity newPlayer = server.getPlayerManager().respawnPlayer(player, true
                                    //#if MC>=12100
                                    //$$ , net.minecraft.entity.Entity.RemovalReason.CHANGED_DIMENSION
                                    //#endif
                            );
                            player.networkHandler.player = newPlayer;
                            c.forEach(effect -> newPlayer.addStatusEffect(new StatusEffectInstance(effect)));
                            return 1;
                        })
        );
    }

}
