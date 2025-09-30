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

package com.ayakacraft.carpetayakaaddition.mixin.rules.betterMobCap;

import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.16"))
@Mixin(SpawnHelper.Info.class)
public interface SpawnHelperInfoAccessor {

    @Contract(pure = true)
    @Invoker("isBelowCap")
    boolean checkBelowCap(
            SpawnGroup group
            //#if MC>=12102
            //#elseif MC>=11800
            , ChunkPos chunkPos
            //#endif
    );

    //#if MC>=12102
    //$$ @Contract(pure = true)
    //$$ @Invoker("canSpawn")
    //$$ boolean checkCanSpawn(SpawnGroup group, ChunkPos chunkPos);
    //#endif

}
