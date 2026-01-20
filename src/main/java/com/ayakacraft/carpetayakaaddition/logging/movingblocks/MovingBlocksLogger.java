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

import com.ayakacraft.carpetayakaaddition.logging.AbstractAyakaLogger;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.StringUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

public class MovingBlocksLogger extends AbstractAyakaLogger {

    public static final String NAME = "movingBlocks";

    public static final MovingBlocksLogger INSTANCE;

    public static final Translator TR = AyakaLoggerRegistry.LOGGER_TR.resolve(NAME);

    private static final String[] OPTIONS = {"full", "brief"};

    private static final short DEFAULT_INDEX = 0;

    static {
        MovingBlocksLogger i = null;
        try {
            i = new MovingBlocksLogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    private MovingBlocksLogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, true);
    }

    private MutableComponent[] doLogging(PistonMovingBlockEntity entity, ServerPlayer player, String option) {
        BlockPos pos = entity.getBlockPos();

        if (OPTIONS[1].equals(option)) {
            return new MutableComponent[]{Component.literal(StringUtils.posString(pos))};
        }

        BlockState       state     = entity.getMovedState();
        Block            block     = state.getBlock();
        MutableComponent direction = TR.tr(player, "direction." + entity.getMovementDirection().getName());

        MutableComponent txt;
        if (block == Blocks.PISTON_HEAD && entity.isExtending()) {
            if (state.getValue(PistonHeadBlock.TYPE) == PistonType.DEFAULT) {
                txt = TR.tr(player, "extend", Blocks.PISTON.getName(), direction, StringUtils.posString(pos));
            } else {
                txt = TR.tr(player, "extend", Blocks.STICKY_PISTON.getName(), direction, StringUtils.posString(pos));
            }
        } else if (entity.isSourcePiston() && !entity.isExtending()) {
            txt = TR.tr(player, "pull_back", state.getBlock().getName(), direction, StringUtils.posString(pos));
        } else {
            txt = TR.tr(player, "common", state.getBlock().getName(), direction, StringUtils.posString(pos));
        }

        return new MutableComponent[]{txt};
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__movingBlocks;
    }

    public void tryLog(PistonMovingBlockEntity pistonBlockEntity) {
        if (isEnabled()) {
            log((playerOption, player) -> doLogging(pistonBlockEntity, (ServerPlayer) player, playerOption));
        }
    }

}
