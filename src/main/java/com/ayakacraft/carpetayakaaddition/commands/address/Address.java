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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;

//Do not remove the lines below
//TODO update in 1.15.2
public class Address extends AbstractAddress<ResourceKey<Level>> {

    public Address(String id, String dim, Vec3 pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    public Address(String id, ResourceKey<Level> dim, Vec3 pos, String desc, long weight) {
        super(id, dim, pos, desc, weight);
    }

    @Contract(pure = true)
    @Override
    protected String transDim(ResourceKey<Level> worldRegistryKey) {
        return worldRegistryKey.identifier().toString();
    }

    @Contract(pure = true)
    @Override
    public ResourceKey<Level> getDimension() {
        return ResourceKey.create(
                //#if MC>=11904
                net.minecraft.core.registries.Registries.DIMENSION,
                //#else
                //$$ net.minecraft.core.Registry.DIMENSION_REGISTRY,
                //#endif
                Identifier.parse(dim)
        );
    }

    @Contract(pure = true)
    @Override
    public boolean isInWorld(Level world) {
        return IdentifierUtils.ofWorld(world).toString().equals(dim);
    }

}
