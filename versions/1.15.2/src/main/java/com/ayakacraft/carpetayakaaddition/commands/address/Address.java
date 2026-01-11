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

package com.ayakacraft.carpetayakaaddition.commands.address;

import com.ayakacraft.carpetayakaaddition.utils.IdentifierUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

public class Address extends AbstractAddress<DimensionType> {

    public Address(String id, String dim, Vec3 pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    public Address(String id, DimensionType dim, Vec3 pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    @Override
    protected String transDim(DimensionType dimensionType) {
        return String.valueOf(DimensionType.getName(dimensionType));
    }

    @Override
    public DimensionType getDimension() {
        return DimensionType.getByName(new ResourceLocation(dim));
    }

    @Override
    public boolean isInWorld(Level world) {
        return String.valueOf(IdentifierUtils.ofWorld(world)).equals(dim);
    }

}
