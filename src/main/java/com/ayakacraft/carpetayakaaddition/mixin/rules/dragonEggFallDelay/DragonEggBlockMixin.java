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

package com.ayakacraft.carpetayakaaddition.mixin.rules.dragonEggFallDelay;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.block.DragonEggBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {

    @ModifyConstant(
            //#if MC>=11600
            method = "getFallDelay",
            //#else
            //$$ method = "getTickRate",
            //#endif
            constant = @Constant(intValue = 5)
    )
    private int modifyFallDelay(int value) {
        return CarpetAyakaSettings.dragonEggFallDelay == 0 ? value : CarpetAyakaSettings.dragonEggFallDelay;
    }

}
