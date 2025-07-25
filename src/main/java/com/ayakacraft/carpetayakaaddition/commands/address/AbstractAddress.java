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

import com.ayakacraft.carpetayakaaddition.utils.MathUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractAddress<T> implements Comparable<AbstractAddress<T>> {

    public static final String DESC_PLACEHOLDER = "#none";

    public static final String XAERO_WAYPOINT_FORMAT = "xaero-waypoint:%s:%s:%d:%d:%d:0:false:0:Internal-%s-waypoints";

    protected final String id;

    protected final String dim;

    protected final double x, y, z;

    @Nullable
    protected String desc;

    protected long weight = 0L;

    public AbstractAddress(String id, String dim, Vec3d pos, String desc, long weight) {
        this.id = id;
        this.dim = dim;
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        setDesc(desc);
        this.weight = weight;
    }

    public AbstractAddress(AddressOld old) {
        this(old.id, old.dim, old.pos, old.desc, 0L);
    }

    public String getDim() {
        return dim;
    }

    public abstract T getDimension();

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

    public ChunkPos getChunkPos() {
        return MathUtils.getChunkPos(getPos());
    }

    public String getXaeroWaypointString() {
        return String.format(
                XAERO_WAYPOINT_FORMAT,
                id, id.charAt(0),
                (int) x, (int) y, (int) z,
                new Identifier(dim).getPath().replace('_', '-')
        );
    }

    public abstract boolean isInWorld(World world);

    public void onDetailDisplayed() {
        weight += 1;
    }

    public void onTeleportedTo() {
        weight += 3;
    }

    @Override
    public int compareTo(@NotNull AbstractAddress<T> o) {
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
