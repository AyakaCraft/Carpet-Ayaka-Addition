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

import net.minecraft.util.math.Vec3d;

public class AddressOld {

    public static final String DESC_PLACEHOLDER = Address.DESC_PLACEHOLDER;

    private final String id;

    private final String dim;

    private final Vec3d pos;

    private String desc;

    public AddressOld(String id, String dim, Vec3d pos, String desc) {
        this.id = id;
        this.dim = dim;
        this.pos = pos;
        setDesc(desc);
    }

    public String getDim() {
        return dim;
    }

    public String getId() {
        return id;
    }

    public Vec3d getPos() {
        return pos;
    }

    public String getDesc() {
        return (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    public void setDesc(String desc) {
        this.desc = (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    @Override
    public String toString() {
        return String.format("address[id=%s, dim=%s, pos=%s, desc=%s]", id, dim, pos, desc);
    }

}
