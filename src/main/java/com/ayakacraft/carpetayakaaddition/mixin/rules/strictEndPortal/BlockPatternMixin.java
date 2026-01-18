/*
 * This file is part of the null project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.mixin.rules.strictEndPortal;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.google.common.cache.LoadingCache;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockPattern.class)
public class BlockPatternMixin {

    @WrapOperation(
            method = "find",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/pattern/BlockPattern;matches(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;Lcom/google/common/cache/LoadingCache;)Lnet/minecraft/world/level/block/state/pattern/BlockPattern$BlockPatternMatch;"
            )
    )
    private BlockPattern.BlockPatternMatch strictMatches(BlockPattern instance, BlockPos blockPos, Direction direction, Direction direction2, LoadingCache<BlockPos, BlockInWorld> loadingCache, Operation<BlockPattern.BlockPatternMatch> original) {
        if (CarpetAyakaSettings.strictEndPortal) {
            if ((Object) this == EndPortalFrameBlockAccessor.getPortalShape()
                    && (direction != Direction.DOWN || direction2 != Direction.SOUTH)) {
                return null;
            }
        }
        return original.call(instance, blockPos, direction, direction2, loadingCache);
    }

}
