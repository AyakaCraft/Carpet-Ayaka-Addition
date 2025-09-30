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

package com.ayakacraft.carpetayakaaddition.mixin.rules.bedrockNoBlastResistance;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ExplosionImpl.class)
public class ExplosionMixin {

    @WrapOperation(
            method = "getBlocksToDestroy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/ExplosionBehavior;getBlastResistance(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Ljava/util/Optional;"
            )
    )
    private Optional<Float> applyBedrockBlastResistance(ExplosionBehavior instance, Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, Operation<Optional<Float>> original) {
        if (CarpetAyakaSettings.bedrockNoBlastResistance && blockState.getBlock() == Blocks.BEDROCK) {
            return Optional.of(fluidState.isEmpty() ? 0f : fluidState.getBlastResistance());
        }
        return original.call(instance, explosion, world, pos, blockState, fluidState);
    }

    @WrapOperation(
            method = "getBlocksToDestroy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/ExplosionBehavior;canDestroyBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;F)Z"
            )
    )
    private boolean makeBedrockUnbreakable(ExplosionBehavior instance, Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power, Operation<Boolean> original) {
        if (CarpetAyakaSettings.bedrockNoBlastResistance && state.getBlock() == Blocks.BEDROCK) {
            return false;
        }
        return original.call(instance, explosion, world, pos, state, power);
    }

}
