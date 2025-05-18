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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

public final class ServerPlayerUtils {

    private static float wrapDegrees(float degrees) {
        float f = degrees % 360.0F;
        if (f >= 180.0F) {
            return (f - 360.0F);
        }

        if (f < -180.0F) {
            return (f + 360.0F);
        }

        return f;
    }

    public static boolean changeGameMode(ServerPlayerEntity player, GameMode gameMode) {
        //#if MC>=11700
        return player.changeGameMode(gameMode);
        //#else
        //$$ if(player.interactionManager.getGameMode() == gameMode) {
        //$$     return false;
        //$$ }
        //$$ player.setGameMode(gameMode);
        //$$ return true;
        //#endif
    }

    public static float getYaw(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getYaw());
        //#else
        //$$ return wrapDegrees(player.getYaw(1f));
        //#endif
    }

    public static float getPitch(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getPitch());
        //#else
        //$$ return wrapDegrees(player.getPitch(1f));
        //#endif
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld dim, double x, double y, double z, float yaw, float pitch) {
        player.teleport(dim, x, y, z,
                //#if MC>=11900
                java.util.Collections.emptySet(),
                //#endif
                yaw, pitch
                //#if MC>=12104
                //$$ , true
                //#endif
        );
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld dim, double x, double y, double z) {
        teleport(player, dim, x, y, z, getYaw(player), getPitch(player));
    }

    public static void teleport(ServerPlayerEntity player, ServerPlayerEntity target) {
        teleport(player, (ServerWorld) target.getEntityWorld(), target.getPos().getX(), target.getPos().getY(), target.getPos().getZ(), getYaw(target), getPitch(target));
    }

    @PreprocessPattern
    public static String getClientLanguage(ServerPlayerEntity player) {
        //#if MC>=12006
        return player.getClientOptions().language();
        //#else
        //$$ return ((com.ayakacraft.carpetayakaaddition.utils.WithClientLanguage) player).getClientLanguage$Ayaka();
        //#endif
    }

}
