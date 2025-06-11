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

package com.ayakacraft.carpetayakaaddition.mixin.rules.legacyHoneyBlockSliding;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">1.21.1"))
@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

    @WrapOperation(
            method = {"updateSlidingVelocity", "isSliding"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65067(D)D", remap = false)
    )
    private double wrapVelocity65067(double v, Operation<Double> original) {
        if (CarpetAyakaSettings.legacyHoneyBlockSliding) {
            return v;
        }
        return original.call(v);
    }

    @WrapOperation(
            method = "updateSlidingVelocity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65068(D)D", remap = false)
    )
    private double wrapVelocity65068(double v, Operation<Double> original) {
        if (CarpetAyakaSettings.legacyHoneyBlockSliding) {
            return v;
        }
        return original.call(v);
    }

}
