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
import com.ayakacraft.carpetayakaaddition.utils.InitializedPerTick;
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class MovingBlocksLogger extends AbstractAyakaLogger implements InitializedPerTick {

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

    private final HashSet<BlockPos> loggedPos = new HashSet<>();

    private MovingBlocksLogger() throws NoSuchFieldException {
        super(NAME, null, new String[0], false);
    }

    private MutableText[] doLogging(PistonBlockEntity entity) {
        BlockPos    pos       = entity.getPos();
        BlockState  state     = entity.getPushedBlock();
        Block       block     = state.getBlock();
        MutableText direction = TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks.direction." + entity.getMovementDirection().getName());

        MutableText txt;
        if (block == Blocks.PISTON_HEAD && entity.isExtending()) {
            if (state.get(PistonHeadBlock.TYPE) == PistonType.DEFAULT) {
                txt = TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks.extend", Blocks.PISTON.getName(), direction, pos.getX(), pos.getY(), pos.getZ());
            } else {
                txt = TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks.extend", Blocks.STICKY_PISTON.getName(), direction, pos.getX(), pos.getY(), pos.getZ());
            }
        } else if (entity.isSource() && !entity.isExtending()) {
            txt = TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks.pull_back", state.getBlock().getName(), direction, pos.getX(), pos.getY(), pos.getZ());
        } else {
            txt = TextUtils.tr("carpet-ayaka-addition.logger.movingBlocks.common", state.getBlock().getName(), direction, pos.getX(), pos.getY(), pos.getZ());
        }

        return new MutableText[]{txt};
    }

    @Override
    public void init() {
        loggedPos.clear();
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__movingBlocks;
    }

    public void tryLog(PistonBlockEntity pistonBlockEntity) {
        if (isEnabled() && !loggedPos.add(pistonBlockEntity.getPos())) {
            log((playerOption, player) -> doLogging(pistonBlockEntity));
        }
    }

}
