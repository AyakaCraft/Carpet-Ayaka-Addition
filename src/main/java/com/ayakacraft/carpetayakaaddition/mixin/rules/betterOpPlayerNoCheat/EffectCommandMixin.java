/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot
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
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.mods.TISHelper;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectCommand.class)
public class EffectCommandMixin {

    @Inject(method = "method_13235", remap = false, at = @At("RETURN"), cancellable = true)
    private static void checkIfAllowCheating_effectCommand(ServerCommandSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && CarpetAyakaSettings.betterOpPlayerNoCheat && ModUtils.isTISLoaded() && !TISHelper.canCheat(source)) { // DO NOT change the order of the conditions
            cir.setReturnValue(false);
        }
    }

}
