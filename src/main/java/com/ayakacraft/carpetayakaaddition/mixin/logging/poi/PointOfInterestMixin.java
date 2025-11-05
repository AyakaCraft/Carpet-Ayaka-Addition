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
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoiRecord.class)
public class PointOfInterestMixin {

    @Shadow
    private int freeTickets;

    @Shadow
    @Final
    private BlockPos pos;

    @Shadow
    @Final
    private
    //#if MC>=11900
    net.minecraft.core.Holder<PoiType> poiType;
    //#else
    //$$ PoiType poiType;
    //#endif

    @Inject(method = "acquireTicket", at = @At("RETURN"))
    private void onTickedReserved(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            POILogger.INSTANCE.onTicketReserved(pos, poiType, freeTickets);
        }
    }

    @Inject(method = "releaseTicket", at = @At("RETURN"))
    private void onTickedReleased(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            POILogger.INSTANCE.onTicketReleased(pos, poiType, freeTickets);
        }
    }

}
