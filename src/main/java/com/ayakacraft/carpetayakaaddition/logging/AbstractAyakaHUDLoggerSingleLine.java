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

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractAyakaHUDLoggerSingleLine extends AbstractAyakaHUDLogger {

    public AbstractAyakaHUDLoggerSingleLine(String logName, String def, String[] options, boolean strictOptions) throws NoSuchFieldException {
        super(logName, def, options, strictOptions);
    }

    @Override
    public MutableComponent[] updateContent(String playerOption, Player player) {
        return new MutableComponent[]{updateSingleLine(playerOption, player)};
    }

    public abstract MutableComponent updateSingleLine(String playerOption, Player player);

}
