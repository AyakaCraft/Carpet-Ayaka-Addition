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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import org.jetbrains.annotations.Contract;

//Do not remove the lines below
//TODO update in 1.15.2
public final class BetterMobCapHelper {

    @Contract(pure = true)
    public static boolean shouldNotLimitSpawning(ServerPlayer instance, EntityType<? extends Entity> entityType) {
        if (!CarpetAyakaSettings.betterMobCap) {
            return true;
        }
        NaturalSpawner.SpawnState info = instance.serverLevel().getChunkSource().getLastSpawnState();
        if (info == null) {
            return true;
        }
        SpawnHelperInfoAccessor accessor   = (SpawnHelperInfoAccessor) info;
        MobCategory             spawnGroup = entityType.getCategory();
        return accessor.checkGlobal$Ayaka(spawnGroup
                //#if MC>=12102
                //#elseif MC>=11800
                , new ChunkPos(instance.blockPosition())
                //#endif
        )
                //#if MC>=12102
                //$$ && accessor.checkLocal$Ayaka(spawnGroup, new ChunkPos(instance.blockPosition()))
                //#endif
                ;
    }

}
