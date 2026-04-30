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

package com.ayakacraft.carpetayakaaddition.mixin.rules.optimizedUpdateSuppressionOutput;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.helpers.rules.SystemReportHelper;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.CrashReport;
import net.minecraft.SystemReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(ModUtils.TIS_ID))
@Mixin(CrashReport.class)
public abstract class CrashReportMixin {

    @WrapOperation(
            method = "<init>",
            at = @At(value = "NEW", target = "()Lnet/minecraft/SystemReport;")
    )
    private SystemReport redirectSystemReport(Operation<SystemReport> original, @Local(argsOnly = true) Throwable exception) {
        if (CarpetAyakaSettings.optimizedUpdateSuppressionOutput && exception instanceof UpdateSuppressionException) {
            return SystemReportHelper.DUMMY_SYSTEM_REPORT;
        }
        return original.call();
    }

}