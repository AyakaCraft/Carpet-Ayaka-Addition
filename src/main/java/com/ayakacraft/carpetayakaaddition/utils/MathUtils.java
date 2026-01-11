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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Contract;

public final class MathUtils {

    @PreprocessPattern
    private static int chunkX(ChunkPos chunkPos) {
        //#if MC>=260000
        //$$ return chunkPos.x();
        //#else
        return chunkPos.x;
        //#endif
    }

    @PreprocessPattern
    private static int chunkZ(ChunkPos chunkPos) {
        //#if MC>=260000
        //$$ return chunkPos.z();
        //#else
        return chunkPos.z;
        //#endif
    }

    @PreprocessPattern
    private static ChunkPos chunkPosOfBlock(BlockPos blockPos) {
        //#if MC>=260000
        //$$ return ChunkPos.containing(blockPos);
        //#else
        return new ChunkPos(blockPos);
        //#endif
    }

    @Contract(pure = true)
    public static ChunkPos getChunkPos(Position pos) {
        return new ChunkPos((int) pos.x() >> 4, (int) pos.z() >> 4);
    }

    @Contract(pure = true)
    public static int getSquaredDistance(ChunkPos pos1, ChunkPos pos2) {
        return square(pos1.x - pos2.x) + square(pos1.z - pos2.z);
    }

    @Contract(pure = true)
    public static int square(int i) {
        return i * i;
    }

    @Contract(pure = true)
    public static float wrapDegrees(float degrees) {
        float f = degrees % 360F;
        return f >= 180F ? (f - 360F) : f < -180F ? (f + 360F) : f;
    }

}
