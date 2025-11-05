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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public final class IdentifierUtils {

    @PreprocessPattern
    private static ResourceLocation of(String id) {
        //#if MC>=12100
        //$$ return ResourceLocation.parse(id);
        //#else
        return new ResourceLocation(id);
        //#endif
    }

    @PreprocessPattern
    private static ResourceLocation of(String namespace, String path) {
        //#if MC>=12100
        //$$ return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //#else
        return new ResourceLocation(namespace, path);
        //#endif
    }

    @PreprocessPattern
    private static ResourceLocation ofWorld(Level world) {
        //#if MC>=11600
        return world.dimension().location();
        //#else
        //$$ return net.minecraft.world.level.dimension.DimensionType.getName(world.getDimension().getType());
        //#endif
    }

}
