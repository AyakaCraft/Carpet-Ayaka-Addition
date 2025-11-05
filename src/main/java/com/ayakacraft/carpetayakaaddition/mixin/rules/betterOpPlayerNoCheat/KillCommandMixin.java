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

package com.ayakacraft.carpetayakaaddition.mixin.rules.betterOpPlayerNoCheat;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.helpers.mods.TISHelper;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.KillCommand;
import org.spongepowered.asm.mixin.Mixin;

//Do not remove the lines below
//TODO update in 1.21.6
@Restriction(require = @Condition(ModUtils.TIS_ID))
@Mixin(KillCommand.class)
public class KillCommandMixin {

    @WrapMethod(method = "method_13432")
    private static boolean checkIfAllowCheating(CommandSourceStack source, Operation<Boolean> original) {
        return !(CarpetAyakaSettings.betterOpPlayerNoCheat && !TISHelper.canCheat(source)) && original.call(source);
    }

}
