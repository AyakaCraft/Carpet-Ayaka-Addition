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

package com.ayakacraft.carpetayakaaddition.utils;

import com.ayakacraft.carpetayakaaddition.utils.preprocess.PreprocessPattern;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

    @PreprocessPattern
    private static boolean isExecutedByPlayer(ServerCommandSource source) {
        //#if MC>=11900
        return source.isExecutedByPlayer();
        //#else
        //$$ return (source.getEntity() instanceof net.minecraft.server.network.ServerPlayerEntity);
        //#endif
    }

    @Contract(pure = true)
    public static boolean checkPermission(ServerCommandSource source, Object commandLevel, boolean requiresPlayer) {
        if (requiresPlayer && !source.isExecutedByPlayer()) {
            return false;
        }

        // Copied from Carpet TIS Addition
        //#if MC>=11900
        return carpet.utils.CommandHelper.canUseCommand(source, commandLevel);
        //#elseif MC>=11600
        //$$ return carpet.settings.SettingsManager.canUseCommand(source, commandLevel);
        //#else
        //$$ if (commandLevel instanceof Boolean) {
        //$$     return (Boolean) commandLevel;
        //$$ }
        //$$ String commandLevelString = commandLevel.toString();
        //$$ switch (commandLevelString) {
        //$$     case "true": return true;
        //$$     case "false": return false;
        //$$     case "ops": return source.hasPermissionLevel(source.getMinecraftServer().getOpPermissionLevel());
        //$$     case "0":
        //$$     case "1":
        //$$     case "2":
        //$$     case "3":
        //$$     case "4":
        //$$         return source.hasPermissionLevel(Integer.parseInt(commandLevelString));
        //$$ }
        //$$ return false;
        //#endif
    }

    @Contract(pure = true)
    public static CompletableFuture<Suggestions> suggestPlayerNames(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder);
    }

    public static void sendFeedback(ServerCommandSource source, Text txt, boolean broadcastToOps) {
        //#if MC>=12000
        source.sendFeedback(() -> txt, broadcastToOps);
        //#else
        //$$ source.sendFeedback(txt, broadcastToOps);
        //#endif
    }

    @Contract(pure = true)
    public static AyakaLanguage getLanguage(ServerCommandSource source) {
        if (source.isExecutedByPlayer()) {
            return ServerPlayerUtils.getLanguage((ServerPlayerEntity) source.getEntity());
        }
        return AyakaLanguage.getServerLanguage();
    }

}
