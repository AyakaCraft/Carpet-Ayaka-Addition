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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Contract;

public final class ServerPlayerUtils {

    @PreprocessPattern
    private static ServerLevel getServerLevel(ServerPlayer player) {
        //#if MC>=12000
        return player.serverLevel();
        //#else
        //$$ return player.getLevel();
        //#endif
    }

    @Contract(pure = true)
    public static String getClientLanguageCode(ServerPlayer player) {
        //#if MC>=12006
        return player.clientInformation().language();
        //#else
        //$$ return ((com.ayakacraft.carpetayakaaddition.utils.translation.WithClientLanguage) player).getClientLanguage$Ayaka();
        //#endif
    }

    @Contract(pure = true)
    public static AyakaLanguage getLanguage(ServerPlayer player) {
        String lang;
        if (player.serverLevel().getServer().isDedicatedServer()) {
            lang = getClientLanguageCode(player);
        } else {
            lang = ClientUtils.getLanguageCode();
        }
        return AyakaLanguage.get(lang);
    }

    @Contract(pure = true)
    public static float getYaw(ServerPlayer player) {
        return MathUtils.wrapDegrees(
                //#if MC>=11700
                player.getYRot()
                //#else
                //$$ player.getViewYRot(1f)
                //#endif
        );
    }

    @Contract(pure = true)
    public static float getPitch(ServerPlayer player) {
        return MathUtils.wrapDegrees(
                //#if MC>=11700
                player.getXRot()
                //#else
                //$$ player.getViewXRot(1f)
                //#endif
        );
    }

    public static boolean changeGameMode(ServerPlayer player, GameType gameMode) {
        //#if MC>=11700
        return player.setGameMode(gameMode);
        //#else
        //$$ if(player.gameMode.getGameModeForPlayer() == gameMode) {
        //$$     return false;
        //$$ }
        //$$ player.setGameMode(gameMode);
        //$$ return true;
        //#endif
    }

    public static void teleport(ServerPlayer player, ServerLevel dim, double x, double y, double z, float yaw, float pitch) {
        player.teleportTo(dim, x, y, z,
                //#if MC>=11900
                java.util.Collections.emptySet(),
                //#endif
                yaw, pitch
                //#if MC>=12102
                //$$ , true
                //#endif
        );
    }

    public static void teleport(ServerPlayer player, ServerLevel dim, double x, double y, double z) {
        teleport(player, dim, x, y, z, getYaw(player), getPitch(player));
    }

    public static void teleport(ServerPlayer player, ServerPlayer target) {
        teleport(player, target.serverLevel(), target.position().x(), target.position().y(), target.position().z(), getYaw(target), getPitch(target));
    }

    public static void sendHome(ServerPlayer player) {
        ServerPlayer newPlayer = player.serverLevel().getServer().getPlayerList().respawn(player,
                //#if MC>=11600
                //#else
                //$$ net.minecraft.world.level.dimension.DimensionType.OVERWORLD,
                //#endif
                true
                //#if MC>=12100
                //$$ , net.minecraft.world.entity.Entity.RemovalReason.CHANGED_DIMENSION
                //#endif
        );
        newPlayer.connection.player = newPlayer;
        EntityUtils.getActiveEffects(newPlayer).putAll(EntityUtils.getActiveEffects(player));
    }

}
