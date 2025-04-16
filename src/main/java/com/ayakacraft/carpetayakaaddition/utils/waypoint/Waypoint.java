/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot
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

package com.ayakacraft.carpetayakaaddition.utils.waypoint;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Waypoint {

    public static final String DESC_NONE = "#none";

    private final String id;

    private final String dim;

    private final Vec3d pos;

    private String desc;

    //#if MC>=11600
    public Waypoint(String id, net.minecraft.registry.RegistryKey<World> dim, Vec3d pos, String desc) {
        this.id = id;
        this.dim = dim.getValue().toString();
        this.pos = pos;
        setDesc(desc);
    }
    //#else
    //$$ public Waypoint(String id, net.minecraft.world.dimension.DimensionType dim, Vec3d pos, String desc) {
    //$$     this.id = id;
    //$$     this.dim = String.valueOf(net.minecraft.world.dimension.DimensionType.getId(dim));
    //$$     this.pos = pos;
    //$$     setDesc(desc);
    //$$ }
    //#endif

    public String getDim() {
        return dim;
    }

    //#if MC>=11600
    public net.minecraft.registry.RegistryKey<World> getDimension() {
        //#if MC>=12100
        //$$ return net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.WORLD, Identifier.of(dim));
        //#elseif MC>=11904
        return net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.WORLD, new Identifier(dim));
        //#else
        //$$ return net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.WORLD_KEY, new Identifier(dim));
        //#endif
    }
    //#else
    //$$ public net.minecraft.world.dimension.DimensionType getDimension() {
    //$$     return net.minecraft.world.dimension.DimensionType.byId(new Identifier(dim));
    //$$ }
    //#endif

    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public double getZ() {
        return pos.getZ();
    }

    public String getId() {
        return id;
    }

    public Vec3d getPos() {
        return pos;
    }

    public String getDesc() {
        return (desc == null || desc.isEmpty()) ? DESC_NONE : desc;
    }

    public void setDesc(String desc) {
        this.desc = (desc == null || desc.isEmpty()) ? DESC_NONE : desc;
    }

}
