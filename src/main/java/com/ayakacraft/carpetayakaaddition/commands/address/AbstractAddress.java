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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractAddress<D> implements Comparable<AbstractAddress<D>> {

    public static final String DESC_PLACEHOLDER = "#none";

    public static final String XAERO_WAYPOINT_FORMAT = "xaero-waypoint:%s:%s:%d:%d:%d:0:false:0:Internal-%s-waypoints";

    @Contract(pure = true)
    public static Address fromOld(AddressOld old) {
        return new Address(old.id, old.dim, old.pos, old.desc, 0);
    }

    protected final String id;

    protected final String dim;

    protected final double x, y, z;

    @Nullable
    protected String desc;

    protected long weight = 0;

    protected boolean pinned = false;

    public AbstractAddress(String id, String dim, Vec3d pos, String desc, long weight) {
        this.id = id;
        this.dim = dim;
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        setDesc(desc);
        this.weight = weight;
    }

    public AbstractAddress(String id, D dim, Vec3d pos, String desc, long weight) {
        this.id = id;
        this.dim = transDim(dim);
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        setDesc(desc);
        this.weight = weight;
    }

    @Contract(pure = true)
    protected abstract String transDim(D d);

    @Contract(pure = true)
    public String getDim() {
        return dim;
    }

    @Contract(pure = true)
    public abstract D getDimension();

    @Contract(pure = true)
    public double getX() {
        return x;
    }

    @Contract(pure = true)
    public double getY() {
        return y;
    }

    @Contract(pure = true)
    public double getZ() {
        return z;
    }

    @Contract(pure = true)
    public String getId() {
        return id;
    }

    @Contract(pure = true)
    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    @Contract(pure = true)
    public String getDesc() {
        return (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    @Contract(pure = true)
    public void setDesc(String desc) {
        this.desc = (desc == null || desc.isEmpty()) ? DESC_PLACEHOLDER : desc;
    }

    @Contract(pure = true)
    public long getWeight() {
        return weight;
    }

    @Contract(pure = true)
    public ChunkPos getChunkPos() {
        return MathUtils.getChunkPos(getPos());
    }

    @Contract(pure = true)
    public String getXaeroWaypointString() {
        return String.format(
                XAERO_WAYPOINT_FORMAT,
                id, id.charAt(0),
                (int) x, (int) y, (int) z,
                new Identifier(dim).getPath().replace('_', '-')
        );
    }

    @Contract(pure = true)
    public abstract boolean isInWorld(World world);

    @Contract(mutates = "this")
    public void onDetailDisplayed() {
        weight += 1;
    }

    @Contract(mutates = "this")
    public void onTeleportedTo() {
        weight += 3;
    }

    @Contract(mutates = "this")
    public void reduceWeight() {
        if (weight > 1) {
            weight = weight * 15 >> 4;
        } else {
            weight = 0;
        }
    }

    @Contract(pure = true)
    public boolean isPinned() {
        return pinned;
    }

    @Contract(mutates = "this")
    public void pin() {
        this.pinned = true;
    }

    @Contract(mutates = "this")
    public void unpin() {
        this.pinned = false;
    }

    @Contract(pure = true)
    @Override
    public int compareTo(@NotNull AbstractAddress<D> o) {
        if (this.equals(o)) {
            return 0;
        }
        return this.weight == o.weight ? this.id.compareTo(o.id) : this.weight > o.weight ? 1 : -1;
    }

    @Contract(pure = true)
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Address other = (Address) o;
        return Objects.equals(other.id, this.id)
                && Objects.equals(other.dim, this.dim)
                && other.getPos().equals(this.getPos())
                && Objects.equals(other.desc, this.desc)
                && other.weight == this.weight;
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return String.format("address[id=%s, dim=%s, pos=[%.2f %.2f %.2f], desc=%s]", id, dim, x, y, z, desc);
    }

}
