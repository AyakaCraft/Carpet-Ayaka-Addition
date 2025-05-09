/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition.commands.gohome;

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

                            final ServerPlayerEntity newPlayer = server.getPlayerManager().respawnPlayer(player,
                                    //#if MC>=11600
                                    //#else
                                    //$$ net.minecraft.world.dimension.DimensionType.OVERWORLD,
                                    //#endif
                                    true
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
