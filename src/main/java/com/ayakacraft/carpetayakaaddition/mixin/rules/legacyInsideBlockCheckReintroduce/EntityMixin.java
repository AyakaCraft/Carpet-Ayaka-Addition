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

package com.ayakacraft.carpetayakaaddition.mixin.rules.legacyInsideBlockCheckReintroduce;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.2"))
@Mixin(Entity.class)
public class EntityMixin {

    //#if MC >= 1.21.8
    @WrapOperation(
            method = "checkInsideBlocks(Ljava/util/List;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;lengthSqr()D"
            )
    )
    private double forceLengthSqrToZero(Vec3 vec3, Operation<Double> original) {
        return CarpetAyakaSettings.legacyInsideBlockCheckReintroduce ? 0.0 : original.call(vec3);
    }
    //#endif

    @WrapOperation(
            method =
                    //#if MC >= 1.21.10
                    "checkInsideBlocks(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;Lit/unimi/dsi/fastutil/longs/LongSet;I)I"
                    //#elseif MC >= 1.21.8
                    //$$ "checkInsideBlocks(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;Lit/unimi/dsi/fastutil/longs/LongSet;)V"
                    //#else
                    //$$ "checkInsideBlocks"
                    //#endif
            ,
            at = @At(
                    value = "INVOKE",
                    target =
                            //#if MC >= 1.21.5
                            "Lnet/minecraft/world/level/BlockGetter;forEachBlockIntersectedBetween(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/BlockGetter$BlockStepVisitor;)Z"
                            //#else
                            //$$ "Lnet/minecraft/world/level/BlockGetter;boxTraverseBlocks(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;)Ljava/lang/Iterable;"
                            //#endif
            )
    )
    private boolean modifyStartPos(Vec3 from, Vec3 to, AABB boundingBox,
                                   //#if MC >= 1.21.5
                                   BlockGetter.BlockStepVisitor visitor,
                                   //#endif
                                   Operation<Boolean> original
    ) {
        return original.call(CarpetAyakaSettings.legacyInsideBlockCheckReintroduce ? to : from, to, boundingBox
                //#if MC >= 1.21.5
                , visitor
                //#endif
        );
    }

}
