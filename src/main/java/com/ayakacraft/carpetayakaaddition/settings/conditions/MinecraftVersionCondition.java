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

package com.ayakacraft.carpetayakaaddition.settings.conditions;

import net.minecraft.MinecraftVersion;

public abstract class MinecraftVersionCondition implements AyakaCondition {

    private static int parseVersion(String name) {
        String[] parts = name.split("\\.", 3);
        return Integer.parseInt(parts[0]) * 10000
                + Integer.parseInt(parts[1]) * 100
                + Integer.parseInt(parts[2]);
    }

    public abstract boolean shouldRegister(int current);

    @Override
    public boolean shouldRegister() {
        //#if MC>=12106
        //$$ return MinecraftVersion.CURRENT.stable() && shouldRegister(parseVersion(MinecraftVersion.CURRENT.name()));
        //#elseif MC>=11600
        return MinecraftVersion.CURRENT.isStable() && shouldRegister(parseVersion(MinecraftVersion.CURRENT.getName()));
        //#else
        //$$ return new MinecraftVersion().isStable() && shouldRegister(parseVersion(new MinecraftVersion().getName()));
        //#endif
    }

}