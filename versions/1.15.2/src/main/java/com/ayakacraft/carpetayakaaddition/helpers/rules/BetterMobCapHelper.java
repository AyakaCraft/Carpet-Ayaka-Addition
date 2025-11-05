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

import carpet.utils.SpawnReporter;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class BetterMobCapHelper {

    private static final int CHUNKS_ELIGIBLE_FOR_SPAWNING = 289;

    public static boolean shouldNotLimitSpawning(ServerPlayer instance, EntityType<? extends Entity> entityType) {
        if (!CarpetAyakaSettings.betterMobCap) {
            return true;
        }
        MobCategory c          = entityType.getCategory();
        ServerLevel world      = instance.getLevel();
        int         chunkCount = SpawnReporter.chunkCounts.getOrDefault(world.getDimension().getType(), 0);
        int         cur        = world.getMobCategoryCounts().getOrDefault(c, -1);
        int         max        = chunkCount * c.getMaxInstancesPerChunk() / CHUNKS_ELIGIBLE_FOR_SPAWNING;
        return cur < max;
    }

}
