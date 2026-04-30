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

package com.ayakacraft.carpetayakaaddition.mixin.rules.legacyArrowHitBlock;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.2"))
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Entity {

    protected AbstractArrowMixin(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
            method = "onHitBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0
            )
    )
    private Vec3 modifyVector(Vec3 instance, double factor, Operation<Vec3> original) {
        if (CarpetAyakaSettings.legacyArrowHitBlock) {
            Vec3 vec = this.position().subtract(this.oldPosition());
            double d = Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
            Vec3 vec2 = d < (double) 1.0E-4F ? Vec3.ZERO : new Vec3(vec.x / d, vec.y / d, vec.z / d);
            return original.call(vec2, factor);
        } else {
            return original.call(instance, factor);
        }
    }

}