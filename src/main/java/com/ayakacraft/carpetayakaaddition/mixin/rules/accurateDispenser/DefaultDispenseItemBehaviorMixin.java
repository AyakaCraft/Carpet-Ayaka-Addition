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

package com.ayakacraft.carpetayakaaddition.mixin.rules.accurateDispenser;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//Do not remove the lines below
//TODO update in 1.18.2
@Mixin(DefaultDispenseItemBehavior.class)
public class DefaultDispenseItemBehaviorMixin {

    @WrapOperation(
            method = "spawnItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/RandomSource;nextDouble()D"
            )
    )
    private static double removeUncertaintyDouble(RandomSource instance, Operation<Double> original) {
        if (CarpetAyakaSettings.accurateDispenser) {
            return 0.5;
        }
        return original.call(instance);
    }

    @WrapOperation(
            method = "spawnItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/RandomSource;triangle(DD)D"
            )
    )
    private static double removeUncertaintyTriangle(RandomSource instance, double d, double e, Operation<Double> original) {
        if (CarpetAyakaSettings.accurateDispenser) {
            return d;
        }
        return original.call(instance, d, e);
    }

}
