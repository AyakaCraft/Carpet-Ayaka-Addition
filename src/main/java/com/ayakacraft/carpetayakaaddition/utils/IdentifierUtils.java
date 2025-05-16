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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class IdentifierUtils {

    @PreprocessPattern
    public static Identifier of(String id) {
        //#if MC>=12100
        //$$ return Identifier.of(id);
        //#else
        return new Identifier(id);
        //#endif
    }

    @PreprocessPattern
    public static Identifier of(String namespace, String path) {
        //#if MC>=12100
        //$$ return Identifier.of(namespace, path);
        //#else
        return new Identifier(namespace, path);
        //#endif
    }

    @PreprocessPattern
    public static Identifier ofWorld(World world) {
        //#if MC>=11600
        return world.getRegistryKey().getValue();
        //#else
        //$$ return net.minecraft.world.dimension.DimensionType.getId(world.getDimension().getType());
        //#endif
    }

}
