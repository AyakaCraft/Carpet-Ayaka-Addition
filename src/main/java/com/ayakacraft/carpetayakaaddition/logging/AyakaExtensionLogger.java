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

package com.ayakacraft.carpetayakaaddition.logging;

import carpet.logging.Logger;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

public interface AyakaExtensionLogger {

    /**
     * Quick access to the field
     */
    @Contract(pure = true)
    default boolean isEnabled() {
        try {
            getField().setAccessible(true);
            return getField().getBoolean(this);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    //#if MC>=11500

    /**
     * Just a placeholder in MC 1.15+
     *
     * @see Logger#getField()
     */
    //#endif
    @Contract(pure = true)
    Field getField();

}
