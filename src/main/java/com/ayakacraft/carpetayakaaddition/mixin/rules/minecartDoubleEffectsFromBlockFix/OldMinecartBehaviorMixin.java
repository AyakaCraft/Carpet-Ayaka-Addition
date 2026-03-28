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

package com.ayakacraft.carpetayakaaddition.mixin.rules.minecartDoubleEffectsFromBlockFix;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

//#if MC<1.21.5
//$$ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
//#endif

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.2"))
@Mixin(OldMinecartBehavior.class)
public class OldMinecartBehaviorMixin {

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;applyEffectsFromBlocks()V"
            )
    )
    private void onlyCheckInsideBlocksOnSecondTime(AbstractMinecart minecart, Operation<Void> original) {
        if (CarpetAyakaSettings.minecartDoubleEffectsFromBlockFix) {
            ((EntityInvoker) minecart).invokeCheckInsideBlocks$Ayaka(List.of(new Entity.Movement(
                            minecart.position(), minecart.position()
                            //#if MC >= 1.21.8 && MC < 1.21.10
                            //$$, false
                            //#endif
                    )),
                    //#if MC >= 1.21.5
                    ((EntityInvoker) minecart).getInsideEffectCollector$Ayaka()
                    //#else
                    //$$ new ReferenceArraySet<>()
                    //#endif
            );
            //#if MC >= 1.21.5
            ((EntityInvoker) minecart).getInsideEffectCollector$Ayaka().applyAndClear(minecart);
            //#endif
        } else original.call(minecart);
    }

}
