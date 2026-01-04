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

package com.ayakacraft.carpetayakaaddition.mixin.logging.poi;

import com.ayakacraft.carpetayakaaddition.logging.poi.POILogger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoiSection.class)
public class PoiSectionMixin {

    @Inject(
            //#if MC>=12109
            method = "add(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/entity/ai/village/poi/PoiRecord;",
            //#elseif MC>=11900
            //$$ method = "add(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Holder;)V",
            //#else
            //$$ method = "add(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/ai/village/poi/PoiType;)V",
            //#endif
            at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V")
    )
    private void onAddedPOI(
            BlockPos pos,
            //#if MC>=11900
            net.minecraft.core.Holder<PoiType> type,
            //#else
            //$$ PoiType type,
            //#endif
            //#if MC>=12109
            org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.entity.ai.village.poi.PoiRecord> cir
            //#else
            //$$ CallbackInfo ci
            //#endif
    ) {
        POILogger.INSTANCE.onAdded(pos, type);
    }

    @Inject(
            method = "remove",
            at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V")
    )
    private void onRemovedPOI(BlockPos pos, CallbackInfo ci) {
        POILogger.INSTANCE.onRemoved(pos);
    }

}
