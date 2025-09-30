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

import com.ayakacraft.carpetayakaaddition.mixin.commands.gohome.LivingEntityAccessor;
import com.ayakacraft.carpetayakaaddition.utils.preprocess.PreprocessPattern;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Contract;

public final class ServerPlayerUtils {

    @PreprocessPattern
    private static String getClientLanguageServerSide(ServerPlayerEntity player) {
        //#if MC>=12006
        return player.getClientOptions().language();
        //#else
        //$$ return ((com.ayakacraft.carpetayakaaddition.utils.translation.WithClientLanguage) player).getClientLanguage$Ayaka();
        //#endif
    }

    @PreprocessPattern
    private static ServerWorld getServerWorld(ServerPlayerEntity player) {
        //#if MC>=12000
        return player.getServerWorld();
        //#elseif MC>=11800
        //$$ return player.getWorld();
        //#else
        //$$ return player.getServerWorld();
        //#endif
    }

    @Contract(pure = true)
    public static AyakaLanguage getLanguage(ServerPlayerEntity player) {
        String lang;
        if (player.getServerWorld().getServer().isDedicated()) {
            lang = player.getClientOptions().language();
        } else {
            lang = ClientUtils.getLanguageCode();
        }
        return AyakaLanguage.get(lang);
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

    @Contract(pure = true)
    public static float getYaw(ServerPlayerEntity player) {
        return MathUtils.wrapDegrees(player.getYaw(
                //#if MC<11700
                //$$ 1f
                //#endif
        ));
    }

    @Contract(pure = true)
    public static float getPitch(ServerPlayerEntity player) {
        return MathUtils.wrapDegrees(player.getPitch(
                //#if MC<11700
                //$$ 1f
                //#endif
        ));
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld dim, double x, double y, double z, float yaw, float pitch) {
        player.teleport(dim, x, y, z,
                //#if MC>=11900
                java.util.Collections.emptySet(),
                //#endif
                yaw, pitch
                //#if MC>=12102
                //$$ , true
                //#endif
        );
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld dim, double x, double y, double z) {
        teleport(player, dim, x, y, z, getYaw(player), getPitch(player));
    }

    public static void teleport(ServerPlayerEntity player, ServerPlayerEntity target) {
        teleport(player, target.getServerWorld(), target.getPos().getX(), target.getPos().getY(), target.getPos().getZ(), getYaw(target), getPitch(target));
    }

    public static void sendHome(ServerPlayerEntity player) {
        ServerPlayerEntity newPlayer = player.getServerWorld().getServer().getPlayerManager().respawnPlayer(player,
                //#if MC>=11600
                //#else
                //$$ net.minecraft.world.dimension.DimensionType.OVERWORLD,
                //#endif
                true
                //#if MC>=12100
                //$$ , net.minecraft.entity.Entity.RemovalReason.CHANGED_DIMENSION
                //#endif
        );
        newPlayer.networkHandler.player = newPlayer;
        ((LivingEntityAccessor) newPlayer).getActiveStatusEffects$Ayaka().putAll(((LivingEntityAccessor) player).getActiveStatusEffects$Ayaka());
    }

}
