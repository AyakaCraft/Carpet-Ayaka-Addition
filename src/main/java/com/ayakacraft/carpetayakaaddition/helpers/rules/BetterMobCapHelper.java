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

package com.ayakacraft.carpetayakaaddition.helpers.rules;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.mixin.rules.betterMobCap.SpawnHelperInfoAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;

//Do not remove the lines below
//TODO update in 1.15.2
public final class BetterMobCapHelper {

    public static boolean shouldSpawn(ServerPlayerEntity instance, EntityType<? extends Entity> entityType) {
        if (!CarpetAyakaSettings.betterMobCap) {
            return true;
        }
        SpawnHelper.Info info = instance.getServerWorld().getChunkManager().getSpawnInfo();
        if (info == null) {
            return true;
        }
        SpawnHelperInfoAccessor accessor   = (SpawnHelperInfoAccessor) info;
        SpawnGroup              spawnGroup = entityType.getSpawnGroup();
        return accessor.checkBelowCap(spawnGroup
                //#if MC>=12104
                //#elseif MC>=11800
                , new ChunkPos(instance.getBlockPos())
                //#endif
        )
                //#if MC>=12104
                //$$ && accessor.checkCanSpawn(spawnGroup, new ChunkPos(instance.getBlockPos()))
                //#endif
                ;
    }

}
