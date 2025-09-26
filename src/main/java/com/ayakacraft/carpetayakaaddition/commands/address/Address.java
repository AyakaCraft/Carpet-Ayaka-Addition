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
import org.jetbrains.annotations.Contract;

//Do not remove the lines below
//TODO update in 1.15.2
public class Address extends AbstractAddress<RegistryKey<World>> {

    public Address(String id, String dim, Vec3d pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    public Address(String id, RegistryKey<World> dim, Vec3d pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    @Contract(pure = true)
    @Override
    protected String transDim(RegistryKey<World> worldRegistryKey) {
        return worldRegistryKey.getValue().toString();
    }

    @Contract(pure = true)
    @Override
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

    @Contract(pure = true)
    @Override
    public boolean isInWorld(World world) {
        return world.getRegistryKey().getValue().toString().equals(dim);
    }

}
