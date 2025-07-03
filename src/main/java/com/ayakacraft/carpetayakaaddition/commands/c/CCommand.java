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

package com.ayakacraft.carpetayakaaddition.commands.c;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.server.command.CommandManager.literal;

public final class CCommand {

    public static final String NAME = "c";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal(NAME)
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandC, true))
                        .executes(ctx -> {
                            final ServerCommandSource         source = ctx.getSource();
                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();
                            if (!ServerPlayerUtils.changeGameMode(player, GameMode.SURVIVAL)) {
                                ServerPlayerUtils.changeGameMode(player, GameMode.SPECTATOR);
                            }
                            return 1;
                        }));
    }

}
