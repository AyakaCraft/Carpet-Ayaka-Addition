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

package com.ayakacraft.carpetayakaaddition.mixin.rules.tickFluids;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(FluidState.class)
public interface FluidStateMixin {

    @WrapMethod(method = "onScheduledTick")
    default void wrapScheduledTick(World world, BlockPos pos, Operation<Void> original) {
        if (CarpetAyakaSettings.tickFluids) {
            original.call(world, pos);
        }
    }

    @WrapMethod(method = "onRandomTick")
    default void wrapRandomTick(World world, BlockPos pos, Random random, Operation<Void> original) {
        if (CarpetAyakaSettings.tickFluids) {
            original.call(world, pos, random);
        }
    }

}
