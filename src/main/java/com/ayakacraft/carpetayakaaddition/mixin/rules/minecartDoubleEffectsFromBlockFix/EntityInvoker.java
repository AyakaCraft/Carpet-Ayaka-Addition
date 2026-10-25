/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026 Calboot and contributors
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

import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.2"))
@Mixin(Entity.class)
public interface EntityInvoker {

    //#if MC >= 1.21.5
    @Accessor("insideEffectCollector")
    net.minecraft.world.entity.InsideBlockEffectApplier.StepBasedCollector getInsideEffectCollector$Ayaka();
    //#endif

    @Invoker("checkInsideBlocks")
    void invokeCheckInsideBlocks$Ayaka(List<Entity.Movement> movements,
                                       //#if MC >= 1.21.5
                                       net.minecraft.world.entity.InsideBlockEffectApplier.StepBasedCollector stepBasedCollector
                                       //#else
                                       //$$ java.util.Set<net.minecraft.world.level.block.state.BlockState> blocksInside
                                       //#endif
    );

}