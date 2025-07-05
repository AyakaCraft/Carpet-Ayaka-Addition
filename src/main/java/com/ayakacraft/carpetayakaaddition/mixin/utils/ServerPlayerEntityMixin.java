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

package com.ayakacraft.carpetayakaaddition.mixin.utils;

import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import com.ayakacraft.carpetayakaaddition.utils.translation.WithClientLanguage;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<1.20.6"))
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements WithClientLanguage {

    @Unique
    private String clientLanguage = AyakaLanguage.getServerLanguage().code();

    @Inject(method = "setClientOptions", at = @At("RETURN"))
    //#if MC>=11800
    private void catchClientLanguage(SyncedClientOptions clientOptions, CallbackInfo ci) {
        clientLanguage = clientOptions.language();
    }
    //#else
    //$$ private void catchClientLanguage(ClientSettingsC2SPacket clientOptions, CallbackInfo ci) {
    //$$     //#if MC>=11600
    //$$     clientLanguage = ((ClientSettingsC2SPacketAccessor) clientOptions).getLanguage();
    //$$     //#else
    //$$     //$$ clientLanguage = clientOptions.getLanguage();
    //$$     //#endif
    //$$ }
    //#endif


    @Override
    @Unique
    public String getClientLanguage$Ayaka() {
        return clientLanguage;
    }

}
