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

package com.ayakacraft.carpetayakaaddition.logging.movingblocks;

import com.ayakacraft.carpetayakaaddition.logging.AbstractAyakaHUDLoggerSingleLine;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.InitializedPerTick;
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class MovingBlocksLogger extends AbstractAyakaHUDLoggerSingleLine implements InitializedPerTick {

    public static final String NAME = "movingBlocks";

    public static final MovingBlocksLogger INSTANCE;

    static {
        MovingBlocksLogger i = null;
        try {
            i = new MovingBlocksLogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    public int movingBlocksCount = 0;

    private MovingBlocksLogger() throws NoSuchFieldException {
        super(NAME, null, new String[0], false);
    }

    @Override
    public void init() {
        movingBlocksCount = 0;
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__movingBlocks;
    }

    @Override
    public MutableText updateSingleLine(String playerOption, PlayerEntity player) {
        //noinspection RedundantCast
        return (MutableText) TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks", movingBlocksCount).formatted(Formatting.GRAY);
    }

}
