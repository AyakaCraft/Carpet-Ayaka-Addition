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

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public final class MathUtils {

    public static ChunkPos getChunkPos(Vec3d pos) {
        return new ChunkPos((int) pos.x >> 4, (int) pos.z >> 4);
    }

    public static int getSquaredDistance(ChunkPos pos1, ChunkPos pos2) {
        return square(pos1.x - pos2.x) + square(pos1.z - pos2.z);
    }

    public static int square(int i) {
        return i * i;
    }

}
