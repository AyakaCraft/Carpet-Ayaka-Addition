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

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

//Do not remove the lines below
//TODO update in 1.15.2
public class Address implements Comparable<Address> {

    public static final String DESC_PLACEHOLDER = "#none";

    private final String id;

    private final String dim;

    private final double x, y, z;

    @Nullable
    private String desc;

    private long weight = 0L;

    public Address(String id, String dim, Vec3d pos, String desc, long weight) {
        this.id = id;
        this.dim = dim;
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        setDesc(desc);
        this.weight = weight;
    }

    public Address(String id, RegistryKey<World> dim, Vec3d pos, String desc, long weight) {
        this(id, dim.getValue().toString(), pos, desc, weight);
    }

    public Address(AddressOld old) {
        this(old.getId(), old.getDim(), old.getPos(), old.getDesc(), 0L);
    }

    public String getDim() {
        return dim;
    }

    public RegistryKey<World> getDimension() {
        return RegistryKey.of(
                //#if MC>=11904
                net.minecraft.registry.RegistryKeys.WORLD,
                //#else
                //$$ net.minecraft.util.registry.Registry.WORLD_KEY,
                //#endif
                new Identifier(dim)
        );
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

    public long getWeight() {
        return weight;
    }

    public void onDetailDisplayed() {
        weight += 1;
    }

    public void onTeleportedTo() {
        weight += 3;
    }

    @Override
    public int compareTo(@NotNull Address o) {
        if (this.equals(o)) {
            return 0;
        }
        if (this.weight == o.weight) {
            return this.id.compareTo(o.id);
        }
        return Long.compare(this.weight, o.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) {
            return false;
        }
        Address other = (Address) o;
        return Objects.equals(other.id, this.id)
                && Objects.equals(other.dim, this.dim)
                && other.getPos().equals(this.getPos())
                && Objects.equals(other.desc, this.desc)
                && other.weight == this.weight;
    }

    @Override
    public String toString() {
        return String.format("address[id=%s, dim=%s, pos=[%.2f %.2f %.2f], desc=%s]", id, dim, x, y, z, desc);
    }

}
