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

package com.ayakacraft.carpetayakaaddition.mixin.rules.frostWalkerNoFreezing;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.entity.ReplaceDiskEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.1"))
@Mixin(ReplaceDiskEnchantmentEffect.class)
public class ReplaceDiskEnchantmentEffectMixin {

    @Shadow
    @Final
    private BlockStateProvider blockState;

    @Unique
    private boolean shouldApply() {
        return !(CarpetAyakaSettings.frostWalkerNoFreezing
                && blockState instanceof SimpleBlockStateProvider
                && blockState.get(null, null).getBlock() == Blocks.FROSTED_ICE);
    }

    @WrapMethod(method = "apply")
    private void wrapApply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos, Operation<Void> original) {
        if (shouldApply()) {
            original.call(world, level, context, user, pos);
        }
    }

}
