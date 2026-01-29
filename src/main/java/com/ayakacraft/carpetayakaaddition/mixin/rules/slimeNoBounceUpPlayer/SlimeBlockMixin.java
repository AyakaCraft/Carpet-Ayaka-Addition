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

package com.ayakacraft.carpetayakaaddition.mixin.rules.slimeNoBounceUpPlayer;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SlimeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

    @ModifyArg(
            //#if MC>=11500
            method = "bounceUp",
            //#else
            //$$ method = "updateEntityAfterFallOn",
            //#endif
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V"),
            index = 1
    )
    private double noBounce(double d, @Local(argsOnly = true) Entity entity) {
        if (CarpetAyakaSettings.slimeNoBounceUpPlayer && entity instanceof Player) {
            return 0D;
        }
        return d;
    }

}
