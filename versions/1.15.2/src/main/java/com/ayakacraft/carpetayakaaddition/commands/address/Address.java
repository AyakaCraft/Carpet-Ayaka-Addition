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

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class Address {

    public static final String DESC_PLACEHOLDER = "#none";

    private final String id;

    private final String dim;

    private final double x, y, z;

    private String desc;

    public Address(String id, String dim, Vec3d pos, String desc) {
        this.id = id;
        this.dim = dim;
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        setDesc(desc);
    }

    public Address(String id, DimensionType dim, Vec3d pos, String desc) {
        this(id, String.valueOf(DimensionType.getId(dim)), pos, desc);
    }

    public Address(AddressOld old) {
        this(old.getId(), old.getDim(), old.getPos(), old.getDesc());
    }

    public String getDim() {
        return dim;
    }

    public DimensionType getDimension() {
        return DimensionType.byId(new Identifier(dim));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getId() {
        return id;
    }

    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    public String getDesc() {
        return (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    public void setDesc(String desc) {
        this.desc = (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    @Override
    public String toString() {
        return String.format("address[id=%s, dim=%s, pos=[%.2f %.2f %.2f], desc=%s]", id, dim, x, y, z, desc);
    }

}
