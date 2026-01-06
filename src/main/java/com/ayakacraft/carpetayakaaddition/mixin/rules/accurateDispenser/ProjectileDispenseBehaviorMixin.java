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
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ProjectileDispenseBehavior.class)
public class ProjectileDispenseBehaviorMixin {

    @ModifyArg(
            method = "execute",
            //#if MC>=12102
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"
            ),
            index = 7
            //#elseif MC>=12006
            //$$ at = @At(
            //$$         value = "INVOKE",
            //$$         target = "Lnet/minecraft/world/item/ProjectileItem;shoot(Lnet/minecraft/world/entity/projectile/Projectile;DDDFF)V"
            //$$ ),
            //$$ index = 5
            //#else
            //$$ at = @At(
            //$$         value = "INVOKE",
            //$$         target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"
            //$$ ),
            //$$ index = 4
            //#endif
    )
    private float removeUncertainty(float uncertainty) {
        if (CarpetAyakaSettings.accurateDispenser) {
            return 0F;
        }
        return uncertainty;
    }

}
