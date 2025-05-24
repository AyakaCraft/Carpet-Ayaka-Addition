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

package com.ayakacraft.carpetayakaaddition.mixin.rules.forceTickPlantsReintroduce;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.16"))
@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {

    @Inject(method = "scheduledTick", at = @At("RETURN"))
    private void onScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (CarpetAyakaSettings.forceTickPlantsReintroduce && state.canPlaceAt(world, pos)) {
            randomTick(state, world, pos, random);
        }
    }

    @Shadow
    //#if MC>=12006
    protected
    //#else
    //$$ public
    //#endif
    abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

}
