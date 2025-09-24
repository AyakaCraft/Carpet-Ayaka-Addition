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

package com.ayakacraft.carpetayakaaddition.mixin.rules.tickFluids;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Do not remove the lines below
//TODO update in 1.15.2
@Mixin(FluidState.class)
public class FluidStateMixin {

    @Inject(method = "onScheduledTick", at = @At("HEAD"), cancellable = true)
    private void wrapScheduledTick(CallbackInfo ci) {
        if (!CarpetAyakaSettings.tickFluids) {
            ci.cancel();
        }
    }

    @Inject(method = "onRandomTick", at = @At("HEAD"), cancellable = true)
    private void wrapRandomTick(CallbackInfo ci) {
        if (!CarpetAyakaSettings.tickFluids) {
            ci.cancel();
        }
    }

}
