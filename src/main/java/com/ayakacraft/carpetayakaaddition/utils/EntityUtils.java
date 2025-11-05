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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class EntityUtils {

    @PreprocessPattern
    private static Vec3 getPos(Entity player) {
        //#if MC>=12109
        //$$ return player.position();
        //#else
        return player.position();
        //#endif
    }

    public static void kill(Entity entity) {
        //#if MC>=12102
        //$$ if (!entity.level().isClientSide()) {
        //$$     entity.kill((net.minecraft.server.level.ServerLevel) entity.level());;
        //$$ }
        //#else
        entity.kill();
        //#endif
    }

}
