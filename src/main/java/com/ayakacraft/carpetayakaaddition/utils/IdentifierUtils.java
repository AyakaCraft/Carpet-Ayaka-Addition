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

package com.ayakacraft.carpetayakaaddition.utils;

import net.minecraft.util.Identifier;

public final class IdentifierUtils {

    public static Identifier ofVanilla(String id) {
        //#if MC>=12100
        //$$ return Identifier.of(id);
        //#else
        return new Identifier(id);
        //#endif
    }

    public static Identifier of(String namespace, String path) {
        //#if MC>=12100
        //$$ return Identifier.of(namespace, path);
        //#else
        return new Identifier(namespace, path);
        //#endif
    }

}
