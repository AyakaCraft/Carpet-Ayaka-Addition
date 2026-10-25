/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026 Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.mixin.rules.kelpGrowOnlyIntoFullWater;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KelpBlock.class)
public class KelpBlockMixin {

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    //#if MC>=11500
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
                    //#else
                    //$$ target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
                    //#endif
            )
    )
    protected boolean checkWaterLevel(
            //#if MC>=11500
            net.minecraft.server.level.ServerLevel instance,
            //#else
            //$$ net.minecraft.world.level.Level instance,
            //#endif
            BlockPos blockPos, BlockState blockState, Operation<Boolean> original) {
        if (!CarpetAyakaSettings.kelpGrowOnlyIntoFullWater || instance.getFluidState(blockPos).getAmount() == 8) {
            return original.call(instance, blockPos, blockState);
        }
        return false;
    }

}
