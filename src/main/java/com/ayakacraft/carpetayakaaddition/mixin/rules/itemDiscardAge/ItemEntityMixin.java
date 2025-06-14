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

package com.ayakacraft.carpetayakaaddition.mixin.rules.itemDiscardAge;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow
    private int itemAge;

    @Unique
    private int getDiscardAge() {
        return CarpetAyakaSettings.itemDiscardAge == 0 ? 6000 : CarpetAyakaSettings.itemDiscardAge;
    }

    @Inject(method = "setDespawnImmediately", at = @At("TAIL"))
    private void onSetDespawnImmediately(CallbackInfo ci) {
        this.itemAge = getDiscardAge() - 1;
    }

    @ModifyConstant(method = {"tick", "canMerge()Z"}, constant = @Constant(intValue = 6000))
    private int modifyDiscardAge(int value) {
        return getDiscardAge();
    }

    @Inject(method = "setCovetedItem", at = @At("TAIL"))
    private void onSetCovetedItem(CallbackInfo ci) {
        this.itemAge = -getDiscardAge();
    }

}
