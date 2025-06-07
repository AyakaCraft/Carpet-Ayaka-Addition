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

package com.ayakacraft.carpetayakaaddition.mixin.logging.movingblocks;

import com.ayakacraft.carpetayakaaddition.logging.movingblocks.MovingBlocksLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    //#if MC>=11700
    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/PistonBlockEntity;markRemoved()V")
    )
    private static void onTick(World world, BlockPos pos, BlockState state, PistonBlockEntity blockEntity, CallbackInfo ci) {
        MovingBlocksLogger.INSTANCE.tryLog(blockEntity);
    }
    //#endif

    @Unique
    private final PistonBlockEntity self = (PistonBlockEntity) (Object) this;

    @Inject(
            method = {
                    "finish"
                    //#if MC<11700
                    //$$ , "tick"
                    //#endif
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/PistonBlockEntity;markRemoved()V")
    )
    private void onFinish(CallbackInfo ci) {
        MovingBlocksLogger.INSTANCE.tryLog(self);
    }

}
