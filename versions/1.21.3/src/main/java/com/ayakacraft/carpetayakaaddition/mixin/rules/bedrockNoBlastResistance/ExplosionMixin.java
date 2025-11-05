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
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerExplosion.class)
public class ExplosionMixin {

    @WrapOperation(
            method = "calculateExplodedPositions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;getBlockExplosionResistance(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Ljava/util/Optional;"
            )
    )
    private Optional<Float> applyBedrockBlastResistance(ExplosionDamageCalculator instance, Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState, Operation<Optional<Float>> original) {
        if (CarpetAyakaSettings.bedrockNoBlastResistance && blockState.getBlock() == Blocks.BEDROCK) {
            return Optional.of(fluidState.isEmpty() ? 0f : fluidState.getExplosionResistance());
        }
        return original.call(instance, explosion, blockGetter, blockPos, blockState, fluidState);
    }

    @WrapOperation(
            method = "calculateExplodedPositions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;shouldBlockExplode(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;F)Z"
            )
    )
    private boolean makeBedrockUnbreakable(ExplosionDamageCalculator instance, Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float power, Operation<Boolean> original) {
        if (CarpetAyakaSettings.bedrockNoBlastResistance && state.getBlock() == Blocks.BEDROCK) {
            return false;
        }
        return original.call(instance, explosion, world, pos, state, power);
    }

}
