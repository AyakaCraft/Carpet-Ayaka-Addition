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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;

import java.lang.reflect.Field;

public interface AyakaExtensionLogger {

    /**
     * Quick access to get the field.
     */
    default boolean isEnabled() {
        try {
            getField().setAccessible(true);
            return getField().getBoolean(this);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    /**
     * Just a placeholder in MC 1.15+
     *
     * @see Logger#getField()
     */
    Field getField();

    MutableText[] updateContent(String playerOption, PlayerEntity player);

    /**
     * Just a placeholder
     *
     * @see Logger#log(Logger.lMessage)
     */
    void log(Logger.lMessage messagePromise);

    default void doLogging() {
        if (isEnabled()) {
            log(this::updateContent);
        }
    }

}
