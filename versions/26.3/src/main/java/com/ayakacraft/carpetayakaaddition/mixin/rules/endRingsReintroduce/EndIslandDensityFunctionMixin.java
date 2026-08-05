/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
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

package com.ayakacraft.carpetayakaaddition.mixin.rules.endRingsReintroduce;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">26.2"))
@Mixin(DensityFunctions.EndIslandDensityFunction.class)
public class EndIslandDensityFunctionMixin {

    @ModifyConstant(
            method = "getHeightValue",
            constant = @Constant(floatValue = -100.0F)
    )
    private static float modifyDoff(
            float doffs,
            @Local(ordinal = 0, argsOnly = true) int sectionX,
            @Local(ordinal = 1, argsOnly = true) int sectionZ
    ) {
        if (CarpetAyakaSettings.endRingsReintroduce)
            return Mth.clamp(100.0F - Mth.sqrt(sectionX * sectionX + sectionZ * sectionZ) * 8.0F, -100.0F, 80.0F);
        return doffs;
    }

}
