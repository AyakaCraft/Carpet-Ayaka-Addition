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
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">1.21.1"))
@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

    @WrapMethod(method = "isSliding")
    private boolean wrapIsSliding(BlockPos pos, Entity entity, Operation<Boolean> original) {
        if (CarpetAyakaSettings.legacyHoneyBlockSliding && !(entity instanceof LivingEntity)) {
            if (entity.isOnGround()) {
                return false;
            }
            if (entity.getY() > (double) pos.getY() + 0.9375 - 1.0E-7) {
                return false;
            }
            if (entity.getVelocity().y >= -0.08) {
                return false;
            }
            double d = Math.abs((double) pos.getX() + 0.5 - entity.getX());
            double e = Math.abs((double) pos.getZ() + 0.5 - entity.getZ());
            double f = 0.4375 + (double) (entity.getWidth() / 2.0f);
            return d + 1.0E-7 > f || e + 1.0E-7 > f;
        }
        return original.call(pos, entity);
    }

    @WrapMethod(method = "updateSlidingVelocity")
    private void wrapUpdateSlidingVelocity(Entity entity, Operation<Void> original) {
        if (CarpetAyakaSettings.legacyHoneyBlockSliding && !(entity instanceof LivingEntity)) {
            Vec3d vec3d = entity.getVelocity();
            if (entity.getVelocity().y < -0.13) {
                double d = -0.05 / entity.getVelocity().y;
                entity.setVelocity(new Vec3d(vec3d.x * d, -0.05, vec3d.z * d));
            } else {
                entity.setVelocity(new Vec3d(vec3d.x, -0.05, vec3d.z));
            }
            entity.onLanding();
        } else {
            original.call(entity);
        }
    }

}
